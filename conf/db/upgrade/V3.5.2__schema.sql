-- ----------------------------
--  add vpcha table
-- ----------------------------
ALTER TABLE `zstack`.`ApplianceVmVO` ADD COLUMN `haStatus` varchar(255) DEFAULT "NoHa";

CREATE TABLE `VpcHaGroupVO` (
    `uuid` VARCHAR(32) NOT NULL UNIQUE,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(2048) DEFAULT NULL,
    `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `VpcHaGroupMonitorIpVO` (
     `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
     `vpcHaRouterUuid` varchar(32) NOT NULL,
     `monitorIp` varchar(255) NOT NULL,
     `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
     `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (`id`),
     CONSTRAINT fkVpcHaGroupMonitorIpVOVpcHaGroupVO FOREIGN KEY (vpcHaRouterUuid) REFERENCES VpcHaGroupVO (uuid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `VpcHaGroupVipRefVO` (
     `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
     `vpcHaRouterUuid` varchar(32) NOT NULL,
     `vipUuid` varchar(32) NOT NULL,
     `l3NetworkUuid` varchar(32) NOT NULL,
     `ip` varchar(32) NOT NULL,
     `netmask` varchar(32) NOT NULL,
     `createDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
     `lastOpDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (`id`),
     CONSTRAINT fkVpcHaGroupVipRefVOVpcHaGroupVO FOREIGN KEY (vpcHaRouterUuid) REFERENCES VpcHaGroupVO (uuid) ON DELETE CASCADE,
     CONSTRAINT fkVpcHaGroupVipRefVOL3NetworkVO FOREIGN KEY (l3NetworkUuid) REFERENCES L3NetworkEO (uuid) ON DELETE CASCADE,
     CONSTRAINT fkVpcHaGroupVipRefVOVipVO FOREIGN KEY (vipUuid) REFERENCES VipVO (uuid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `zstack`.`VpcHaGroupApplianceVmRefVO` (
    `uuid` varchar(32) NOT NULL UNIQUE,
    `vpcHaRouterUuid` varchar(32) DEFAULT NULL,
    PRIMARY KEY  (`uuid`),
    CONSTRAINT fkVpcHaGroupApplianceVmRefVOVpcHaGroupVO FOREIGN KEY (vpcHaRouterUuid) REFERENCES VpcHaGroupVO (uuid) ON DELETE CASCADE,
    CONSTRAINT fkVpcHaGroupApplianceVmRefVOApplianceVmVO FOREIGN KEY (uuid) REFERENCES ApplianceVmVO (uuid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `zstack`.`VpcHaGroupNetworkServiceRefVO` (
    `id` bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `vpcHaRouterUuid` varchar(32) NOT NULL,
    `networkServiceName` varchar(128) NOT NULL,
    `networkServiceUuid` varchar(128) NOT NULL,
    `lastOpDate` timestamp ON UPDATE CURRENT_TIMESTAMP,
    `createDate` timestamp,
    PRIMARY KEY (`id`),
    CONSTRAINT fkVpcHaGroupNetworkServiceRefVOVpcHaGroupVO FOREIGN KEY (vpcHaRouterUuid) REFERENCES VpcHaGroupVO (uuid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `zstack`.`NetworkRouterAreaRefVO` DROP FOREIGN KEY fkNetworkRouterAreaRefVOVpcRouterVmVO;
ALTER TABLE `zstack`.`NetworkRouterAreaRefVO` ADD COLUMN `applianceVmType` varchar(255) DEFAULT "vpcvrouter";

ALTER TABLE `zstack`.`IAM2OrganizationVO` ADD COLUMN `srcType` varchar(32) DEFAULT NULL;
UPDATE `zstack`.`IAM2OrganizationVO` SET `srcType` = "ZStack" WHERE `uuid`
 NOT IN (SELECT `resourceUuid` FROM `LdapResourceRefVO` WHERE `resourceType`='IAM2OrganizationVO');
UPDATE `zstack`.`IAM2OrganizationVO` SET `srcType` = "Ldap" WHERE `uuid`
 IN (SELECT `resourceUuid` FROM `LdapResourceRefVO` WHERE `resourceType`='IAM2OrganizationVO');

ALTER TABLE `zstack`.`PciDeviceVO` ADD COLUMN `iommuGroup` VARCHAR(255) DEFAULT NULL;
