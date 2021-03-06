package org.zstack.header.vm

import org.zstack.header.errorcode.ErrorCode
import org.zstack.header.vm.VmNicInventory

doc {

	title "更改网卡驱动返回"

	ref {
		name "error"
		path "org.zstack.header.vm.APIUpdateVmNicDriverEvent.error"
		desc "错误码，若不为null，则表示操作失败, 操作成功时该字段为null",false
		type "ErrorCode"
		since "3.9"
		clz ErrorCode.class
	}
	ref {
		name "inventory"
		path "org.zstack.header.vm.APIUpdateVmNicDriverEvent.inventory"
		desc "null"
		type "VmNicInventory"
		since "3.9"
		clz VmNicInventory.class
	}
}
