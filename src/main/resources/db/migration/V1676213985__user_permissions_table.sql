
-- TABLES


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`                int NOT NULL AUTO_INCREMENT,
    `user_name`         varchar(15) NOT NULL,
    `first_name`        varchar(75) NOT NULL,
    `last_name`         varchar(75) NOT NULL,
    `email`             varchar(25) NOT NULL,
    `password`          varchar(127) NOT NULL,
    `mobile`            varchar(15) NOT NULL,
    `is_active`         boolean DEFAULT TRUE,
    `description`       varchar(175) DEFAULT NULL,
    `role_id`           int NOT NULL,
    `created_by`        int NOT NULL,
    `modified_by`       int NOT NULL,
    `creation_time`     datetime DEFAULT CURRENT_TIMESTAMP,
    `modification_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;



DROP TABLE IF EXISTS `sessions`;
CREATE TABLE `sessions` (
  `id`          int(11) NOT NULL AUTO_INCREMENT,
  `user_id`     int(11) NOT NULL,
  `session_id`  varchar(100) NOT NULL,
  `time_stamp`  datetime NOT NULL,
  `expires_at`  datetime NOT NULL,
  `created_at`  datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `sessions_of_user_FK` (`user_id`),
  CONSTRAINT `sessions_of_user_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);


DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`                int NOT NULL AUTO_INCREMENT,
    `role_name`         varchar(75) NOT NULL,
    `description`       varchar(150) DEFAULT NULL,
    `created_by`        int NOT NULL,
    `modified_by`       int NOT NULL,
    `creation_time`     datetime DEFAULT CURRENT_TIMESTAMP,
    `modification_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;

DROP TABLE IF EXISTS `workflow`;
CREATE TABLE `workflow`
(
    `id`                int NOT NULL AUTO_INCREMENT,
    `workflow_id`       varchar(75) NOT NULL,
    `description`       varchar(150) NOT NULL,
    `version`           int NOT NULL,
    `workflow_json`     blob NOT NULL,
    `created_by`        int NOT NULL,
    `modified_by`       int NOT NULL,
    `creation_time`     datetime DEFAULT CURRENT_TIMESTAMP,
    `modification_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;



DROP TABLE IF EXISTS `workorder`;
CREATE TABLE `workorder`
(
    `id`                int NOT NULL AUTO_INCREMENT,
    `workorder_id`      varchar(75) NOT NULL,
    `status`            enum ('NOT_STARTED','IN_PROGRESS','COMPLETED','CLOSED') DEFAULT 'NOT_STARTED',
    `is_active`         boolean DEFAULT TRUE,
    `meta_data`         blob DEFAULT NULL,
    `workflow_id_fk`    int NOT NULL,
    `assigned_to`       int NOT NULL,
    `created_by`        int NOT NULL,
    `modified_by`       int NOT NULL,
    `creation_time`     datetime DEFAULT CURRENT_TIMESTAMP,
    `modification_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;



DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`
(
    `id`                int NOT NULL AUTO_INCREMENT,
    `task_id`           varchar(100) NOT NULL,
    `workorder_id_fk`   int NOT NULL,
    `title`             varchar(100),
    `remark`            varchar(175) DEFAULT NULL,
    `status`            enum ('NOT_STARTED','IN_PROGRESS','COMPLETED','RE_OPENED', 'CLOSED') DEFAULT 'NOT_STARTED',
    `assigned_to`       int NOT NULL,
    `is_active`         boolean DEFAULT TRUE,
    `task_number`       boolean NOT NULL,
    `meta_data`         blob DEFAULT NULL,
    `completed_by`      int DEFAULT
     NULL,
    `created_by`        int NOT NULL,
    `modified_by`       int NOT NULL,
    `creation_time`     datetime DEFAULT CURRENT_TIMESTAMP,
    `modification_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;



DROP TABLE IF EXISTS `item_type`;
CREATE TABLE `item_type`
(
    `id`            int NOT NULL AUTO_INCREMENT,
    `name`          varchar(25) NOT NULL,
    `description`   varchar(125) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;



DROP TABLE IF EXISTS `id_type`;
CREATE TABLE `id_type`
(
    `id`            int NOT NULL AUTO_INCREMENT,
    `id_type`       varchar(25) NOT NULL,
    `description`   varchar(125) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;



DROP TABLE IF EXISTS `vendor_type`;
CREATE TABLE `vendor_type`
(
    `id`            int NOT NULL AUTO_INCREMENT,
    `vendor_type`   varchar(25) NOT NULL,
    `description`   varchar(125) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;



DROP TABLE IF EXISTS `vendor`;
CREATE TABLE `vendor`
(
    `id`                int NOT NULL AUTO_INCREMENT,
    `first_name`        varchar(75) DEFAULT NULL,
    `last_name`         varchar(75) DEFAULT NULL,
    `address`           varchar(175) DEFAULT NULL,
    `city`              varchar(25) DEFAULT NULL,
    `state`             varchar(25) DEFAULT NULL,
    `email`             varchar(25) DEFAULT NULL,
    `mobile`            varchar(15) DEFAULT NULL,
    `vendor_img`        varchar(120) DEFAULT NULL,
    `vendor_type_fk`    int NOT NULL,
    `id_type_fk`        int NOT NULL,
    `id_number`         varchar(75) DEFAULT NULL,
    `id_img`            varchar(120) DEFAULT NULL,
    `created_by`        int NOT NULL,
    `modified_by`       int NOT NULL,
    `creation_time`     datetime DEFAULT CURRENT_TIMESTAMP,
    `modification_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;



DROP TABLE IF EXISTS `purchase`;
CREATE TABLE `purchase`
(
    `id`                int NOT NULL AUTO_INCREMENT,
    `item_type_fk`      int NOT NULL,
    `workorder_id_fk`   int NOT NULL,
    `vendor_id_fk`      int NOT NULL,
    `price`             decimal NOT NULL,
    `weight`            decimal NOT NULL,
    `weight_unit`       enum ('KG','QUINTAL','TON') DEFAULT 'KG',
    `created_by`        int NOT NULL,
    `modified_by`       int NOT NULL,
    `creation_time`     datetime DEFAULT CURRENT_TIMESTAMP,
    `modification_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = latin1;





--  INITAL DATA

INSERT INTO `role`(id, role_name, description, created_by, modified_by)
VALUES (1, 'ROLE_ADMIN', 'This is Administrator role. This is the highest level Role in the organization which has all the permissions', 1, 1);

INSERT INTO `role`(id,role_name, description, created_by, modified_by)
VALUES (2, 'RL_PROCUREMENT_GENERATE_TOKEN', 'This will be assigned to generate the token during procurement process.', 1, 1);

INSERT INTO `role`(id,role_name, description, created_by, modified_by)
VALUES (3, 'RL_PROCUREMENT_SAMPLE_GRADING', 'This will be assigned to grade the item sample.', 1, 1);

INSERT INTO `role`(id,role_name, description, created_by, modified_by)
VALUES (4, 'RL_PROCUREMENT_ITEM_WEIGHING', 'This will be assigned to weigh the vendor item/vehicle.', 1, 1);

INSERT INTO `role`(id,role_name, description, created_by, modified_by)
VALUES (5, 'RL_PROCUREMENT_UNLOADING', 'This will be assigned to unload the vendor item/vehicle.', 1, 1);

INSERT INTO `user`(user_name, first_name, last_name, email, password, mobile, is_active, description, role_id, created_by, modified_by)
VALUES ('admin', 'Admin', 'Admin', 'kapas_2023@gmail.com', '$2a$10$l3DiOh2JYd9pH9UY5MIziuJ4dNr0reoPncGkow9a1Ke/OSr7hMjl2', '999-999-9999', 1, 'This is Admin User.', 1, 1, 1);

INSERT INTO `user`(user_name, first_name, last_name, email, password, mobile, is_active, description, role_id, created_by, modified_by)
VALUES ('Alex', 'Alex', '', 'alex@gmail.com', '$2a$10$l3DiOh2JYd9pH9UY5MIziuJ4dNr0reoPncGkow9a1Ke/OSr7hMjl2', '999-999-9999', 1,
'This user can generate tokens during procurement process.', 2, 1, 1);

INSERT INTO `user`(user_name, first_name, last_name, email, password, mobile, is_active, description, role_id, created_by, modified_by)
VALUES ('Bob', 'Alex', '', 'bob@gmail.com', '$2a$10$l3DiOh2JYd9pH9UY5MIziuJ4dNr0reoPncGkow9a1Ke/OSr7hMjl2', '999-999-9999', 1,
'This user can grade sample during procurement process.', 3, 1, 1);

INSERT INTO `user`(user_name, first_name, last_name, email, password, mobile, is_active, description, role_id, created_by, modified_by)
VALUES ('Chris', 'Alex', '', 'chris@gmail.com', '$2a$10$l3DiOh2JYd9pH9UY5MIziuJ4dNr0reoPncGkow9a1Ke/OSr7hMjl2', '999-999-9999', 1,
'This user can weigh item/vehicle during procurement process.', 3, 1, 1);

INSERT INTO `user`(user_name, first_name, last_name, email, password, mobile, is_active, description, role_id, created_by, modified_by)
VALUES ('Dave', 'Alex', '', 'dave@gmail.com', '$2a$10$l3DiOh2JYd9pH9UY5MIziuJ4dNr0reoPncGkow9a1Ke/OSr7hMjl2', '999-999-9999', 1,
'This user can unload item/vehicle during procurement process.', 4, 1, 1);

INSERT INTO `workflow`(workflow_id, description, version, workflow_json, created_by, modified_by)
VALUES ('SHRI_BALAJI_AGR_PROCUREMENT_PROCESS',
'This workflow is for the digital automation of the currently followed procuerement process at Shri Balaji Agro Pvt Ltd.', 1,
'{"workflowId":"SHRI_BALAJI_AGR_PROCUREMENT_PROCESS","totalTask":4,"assignedToRoleName":"RL_PROCUREMENT_UNLOADING","taskList":[{"taskNumber":1,"taskId":"GENERATE_TOKEN","assignedToRoleName":"RL_PROCUREMENT_GENERATE_TOKEN","taskName":"GenerateToken","description":"Thistaskisassignedtousertoinsertvendor/vehicledetailsandgeneratetoken.","isRequired":true,"fields":[{"name":"vendor_fname","type":"string","isRequired":true},{"name":"vendor_lname","type":"string","isRequired":true},{"name":"vendor_address","type":"string","isRequired":true},{"name":"vendor_state","type":"string","isRequired":true},{"name":"vendor_city","type":"string","isRequired":true},{"name":"vendor_mobile","type":"string","isRequired":true},{"name":"vendor_id_type","type":"string","isRequired":true},{"name":"vendor_id","type":"string","isRequired":true},{"name":"vendor_vehicle","type":"string","isRequired":true},{"name":"vendor_img","type":"string","isRequired":true},{"name":"vendor_vehicle_img","type":"string","isRequired":true}]},{"taskNumber":2,"taskId":"GRADING","assignedToRoleName":"RL_PROCUREMENT_SAMPLE_GRADING","taskName":"SamplingandGrading","description":"Thistaskisassignedtousertodecidethecottonsamplegradeandinsertgrade/pricedetails.","isRequired":true,"fields":[{"name":"grade","type":"string","isRequired":true},{"name":"price","type":"double","isRequired":true},{"name":"price_unit","type":"string","isRequired":true}]},{"taskNumber":3,"taskId":"WEIGHING","assignedToRoleName":"RL_PROCUREMENT_ITEM_WEIGHING","taskName":"CottonWeighing","description":"ThistaskisassignedtousertoinsertINandOUTweightofcottoncarryingvehicle.","isRequired":true,"fields":[{"name":"in_weight","type":"double","isRequired":true},{"name":"out_weight","type":"double","isRequired":true},{"name":"weigh_unit","type":"string","isRequired":true}]},{"taskNumber":4,"taskId":"UNLOADING","assignedToRoleName":"RL_PROCUREMENT_UNLOADING","taskName":"CottonUnloading","description":"ThistaskisassignedtousertoinsertINandOUTweightofcottoncarryingvehicle.","isRequired":true,"fields":[{"name":"in_weight","type":"double","isRequired":true},{"name":"out_weight","type":"double","isRequired":true},{"name":"weigh_unit","type":"string","isRequired":true}]}]}', 1, 1);

-- CONSTRAINTS

ALTER TABLE `user`
    ADD CONSTRAINT `user_role_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);

ALTER TABLE `user`
    ADD CONSTRAINT `user_creation_user_fk` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`);

ALTER TABLE `user`
    ADD CONSTRAINT `user_modification_user_fk` FOREIGN KEY (`modified_by`) REFERENCES `user` (`id`);

ALTER TABLE `role`
    ADD CONSTRAINT `role_creation_user_fk` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`);

ALTER TABLE `role`
    ADD CONSTRAINT `role_modification_user_fk` FOREIGN KEY (`modified_by`) REFERENCES `user` (`id`);

ALTER TABLE `workorder`
    ADD CONSTRAINT `workorder_workflow_fk` FOREIGN KEY (`workflow_id_fk`) REFERENCES `workflow` (`id`);

ALTER TABLE `workorder`
    ADD CONSTRAINT `workorder_assigned_role_fk` FOREIGN KEY (`assigned_to`) REFERENCES `role` (`id`);

ALTER TABLE `workorder`
    ADD CONSTRAINT `workorder_creation_user_fk` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`);

ALTER TABLE `workorder`
    ADD CONSTRAINT `workorder_modification_user_fk` FOREIGN KEY (`modified_by`) REFERENCES `user` (`id`);

ALTER TABLE `task`
    ADD CONSTRAINT `task_workorder_fk` FOREIGN KEY (`workorder_id_fk`) REFERENCES `workorder` (`id`);

ALTER TABLE `task`
    ADD CONSTRAINT `task_assigned_role_fk` FOREIGN KEY (`assigned_to`) REFERENCES `role` (`id`);

ALTER TABLE `task`
    ADD CONSTRAINT `task_completed_by_user_fk` FOREIGN KEY (`completed_by`) REFERENCES `user` (`id`);

ALTER TABLE `task`
    ADD CONSTRAINT `task_creation_user_fk` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`);

ALTER TABLE `task`
    ADD CONSTRAINT `task_modification_user_fk` FOREIGN KEY (`modified_by`) REFERENCES `user` (`id`);

ALTER TABLE `vendor`
    ADD CONSTRAINT `vendor_vendor_type_fk` FOREIGN KEY (`vendor_type_fk`) REFERENCES `vendor_type` (`id`);

ALTER TABLE `vendor`
    ADD CONSTRAINT `vendor_id_type_fk` FOREIGN KEY (`id_type_fk`) REFERENCES `id_type` (`id`);

ALTER TABLE `vendor`
    ADD CONSTRAINT `vendor_creation_user_fk` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`);

ALTER TABLE `vendor`
    ADD CONSTRAINT `vendor_modification_user_fk` FOREIGN KEY (`modified_by`) REFERENCES `user` (`id`);

ALTER TABLE `purchase`
    ADD CONSTRAINT `purchase_item_type_fk` FOREIGN KEY (`item_type_fk`) REFERENCES `item_type` (`id`);

ALTER TABLE `purchase`
    ADD CONSTRAINT `purchase_workorder_type_fk` FOREIGN KEY (`workorder_id_fk`) REFERENCES `workorder` (`id`);

ALTER TABLE `purchase`
    ADD CONSTRAINT `purchase_vendor_fk` FOREIGN KEY (`vendor_id_fk`) REFERENCES `vendor` (`id`);

ALTER TABLE `purchase`
    ADD CONSTRAINT `purchase_creation_user_fk` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`);

ALTER TABLE `purchase`
    ADD CONSTRAINT `purchase_modification_user_fk` FOREIGN KEY (`modified_by`) REFERENCES `user` (`id`);



-- DELETE CONSTRAINTS -- PLEASE KEEP COMMENTED
-- ALTER TABLE `user` DROP CONSTRAINT `user_role_fk`
-- ALTER TABLE `user` DROP CONSTRAINT `user_creation_user_fk`
-- ALTER TABLE `user` DROP CONSTRAINT `user_modification_user_fk`
-- ALTER TABLE `role` DROP CONSTRAINT `role_creation_user_fk`
-- ALTER TABLE `role` DROP CONSTRAINT `role_modification_user_fk`
-- ALTER TABLE `permission` DROP CONSTRAINT `permission_creation_user_fk`
-- ALTER TABLE `permission` DROP CONSTRAINT `permission_modification_user_fk`
-- ALTER TABLE `role_permission` DROP CONSTRAINT `permission_fk`
-- ALTER TABLE `role_permission` DROP CONSTRAINT `role_fk`