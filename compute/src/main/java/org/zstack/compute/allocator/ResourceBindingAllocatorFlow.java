package org.zstack.compute.allocator;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.StringUtils;
import org.zstack.compute.vm.VmGlobalConfig;
import org.zstack.compute.vm.VmSystemTags;
import org.zstack.core.Platform;
import org.zstack.core.componentloader.PluginRegistry;
import org.zstack.header.allocator.AbstractHostAllocatorFlow;
import org.zstack.header.allocator.AllocationScene;
import org.zstack.header.allocator.ResourceBindingCollector;
import org.zstack.header.allocator.ResourceBindingStrategy;
import org.zstack.header.exception.CloudRuntimeException;
import org.zstack.header.host.HostVO;
import org.zstack.resourceconfig.ResourceConfigFacade;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ Author : yh.w
 * @ Date   : Created in 18:11 2019/11/26
 */
@Configurable(preConstruction = true, autowire = Autowire.BY_TYPE)
public class ResourceBindingAllocatorFlow extends AbstractHostAllocatorFlow {

    @Autowired
    protected PluginRegistry pluginRgty;
    @Autowired
    private ResourceConfigFacade rcf;

    private static Map<String, ResourceBindingCollector> collectors = Collections.synchronizedMap(new HashMap<>());

    private static String SPLIT = ",";

    {
        List<ResourceBindingCollector> cs = pluginRgty.getExtensionList(ResourceBindingCollector.class);
        for (ResourceBindingCollector collector : cs) {
            collectors.put(collector.getType(), collector);
        }
    }

    private Map<String, List<String>> getBindedResources() {
        String resources = VmSystemTags.VM_RESOURCE_BINGDING
                .getTokenByResourceUuid(spec.getVmInstance().getUuid(), VmSystemTags.VM_RESOURCE_BINGDING_TOKEN);
        if (StringUtils.isEmpty(resources)) {
            return null;
        }

        Map<String, List<String>> resourceMap = new HashMap<>();
        for (String resource : resources.split(SPLIT)) {
            String type = resource.split(":")[0];
            String uuid = resource.split(":")[1];
            List<String> resourceList = resourceMap.computeIfAbsent(type, k -> new ArrayList<>());
            resourceList.add(uuid);
        }

        return resourceMap;
    }

    private boolean validateAllocationScene() {
        String as = rcf.getResourceConfigValue(VmGlobalConfig.RESOURCE_BINDING_SCENE, spec.getVmInstance().getUuid(), String.class);
        if (as.equals(AllocationScene.All.toString())) {
            return true;
        }

        if (spec.getAllocationScene() != null) {
            return as.equals(spec.getAllocationScene().toString());
        }

        return false;
    }

    @Override
    public void allocate() {
        if (amITheFirstFlow()) {
            throw new CloudRuntimeException("ResourceBindingAllocatorFlow cannot be the first flow in the chain");
        }

        if (!validateAllocationScene() || !VmSystemTags.VM_RESOURCE_BINGDING.hasTag(spec.getVmInstance().getUuid())) {
            next(candidates);
            return;
        }

        Map<String, List<String>> resources = getBindedResources();
        if (resources == null || resources.isEmpty()) {
            next(candidates);
            return;
        }

        List<HostVO> availableHost = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : resources.entrySet()) {
            ResourceBindingCollector collector = collectors.get(entry.getKey());
            if (collector == null) {
                fail(Platform.operr("resource binding not support type %s yet", entry.getKey()));
                return;
            }
            availableHost.addAll(collector.collect(entry.getValue()));
        }

        List<HostVO> filteredHost = candidates.stream()
                .filter(v -> availableHost.stream().anyMatch(h -> h.getUuid().equals(v.getUuid())))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(filteredHost)) {
            next(filteredHost);
            return;
        }

        if (rcf.getResourceConfigValue(VmGlobalConfig.RESOURCE_BINDING_STRATEGY, spec.getVmInstance().getUuid(), String.class)
                .equals(ResourceBindingStrategy.Soft.toString())) {
            next(candidates);
        } else {
            fail(Platform.operr("no available host found with binded resource %s", resources));
        }
    }
}
