package org.zstack.header.vm

import org.zstack.header.vm.APIUpdateVmNicDriverEvent

doc {
    title "UpdateVmNicDriver"

    category "vmInstance"

    desc """更改网卡驱动"""

    rest {
        request {
			url "PUT /vm-instances/{vmInstanceUuid}/actions"

			header (Authorization: 'OAuth the-session-uuid')

            clz APIUpdateVmNicDriverMsg.class

            desc """"""
            
			params {

				column {
					name "vmInstanceUuid"
					enclosedIn "params"
					desc "云主机UUID"
					location "url"
					type "String"
					optional false
					since "3.9"
					
				}
				column {
					name "vmNicUuid"
					enclosedIn "params"
					desc "云主机网卡UUID"
					location "url"
					type "String"
					optional false
					since "3.9"
					
				}
				column {
					name "driverType"
					enclosedIn "params"
					desc ""
					location "body"
					type "String"
					optional false
					since "3.9"
				}
				column {
					name "systemTags"
					enclosedIn ""
					desc ""
					location "body"
					type "List"
					optional true
					since "3.9"
					
				}
				column {
					name "userTags"
					enclosedIn ""
					desc ""
					location "body"
					type "List"
					optional true
					since "3.9"
					
				}
			}
        }

        response {
            clz APIUpdateVmNicDriverEvent.class
        }
    }
}