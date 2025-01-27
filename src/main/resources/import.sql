INSERT INTO "doctoralcenterrole" ("id", "role")
SELECT nextval('doctoralCenterRole_id_seq'), 'администратор'
WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'администратор');

INSERT INTO "doctoralcenterrole" ("id", "role")
SELECT nextval('doctoralCenterRole_id_seq'), 'експерт'
WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'експерт');

INSERT INTO "doctoralcenterrole" ("id", "role")
SELECT nextval('doctoralCenterRole_id_seq'), 'ръководител'
WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'ръководител');

INSERT INTO "doctoralcenter" ("id", "oid", "name", "email", "picture", "role")
				SELECT
								nextval('doctoralCenter_id_seq'),
								'de6b478e-e4e4-422d-88fb-293fe69c6519',
								'администратор',
								's19621609@onlineedu.tu-varna.bg',
								'doctoralCenter_image.png',
								role.id
				FROM "doctoralcenterrole" role
				WHERE role.role = 'администратор'
				AND NOT EXISTS (
								SELECT 1
								FROM "doctoralcenter"
								WHERE "oid" = 'de6b478e-e4e4-422d-88fb-293fe69c6519');

INSERT INTO "phdstatus" ("id", "status")
SELECT nextval('PhdStatus_id_seq'), 'записан'
WHERE NOT EXISTS (SELECT 1 FROM "phdstatus" WHERE "status" = 'записан');

INSERT INTO "phdstatus" ("id", "status")
SELECT nextval('PhdStatus_id_seq'), 'дипломиран'
WHERE NOT EXISTS (SELECT 1 FROM "phdstatus" WHERE "status" = 'дипломиран');

INSERT INTO "phdstatus" ("id", "status")
SELECT nextval('PhdStatus_id_seq'), 'терминиран'
WHERE NOT EXISTS (SELECT 1 FROM "phdstatus" WHERE "status" = 'терминиран');


INSERT INTO "committeerole" ("id", "type")
SELECT nextval('committeeRole_id_seq'), 'прецедател'
WHERE NOT EXISTS (SELECT 1 FROM "committeerole" WHERE "type" = 'прецедател');

INSERT INTO "committeerole" ("id", "type")
SELECT nextval('committeeRole_id_seq'), 'член'
WHERE NOT EXISTS (SELECT 1 FROM "committeerole" WHERE "type" = 'член');


INSERT INTO "mode" ("id", "mode")
SELECT nextval('mode_id_seq'), 'редовно'
WHERE NOT EXISTS (SELECT 1 FROM "mode" WHERE "mode" = 'редовно');

INSERT INTO "mode" ("id", "mode")
SELECT nextval('mode_id_seq'), 'задочно'
WHERE NOT EXISTS (SELECT 1 FROM "mode" WHERE "mode" = 'задочно');


INSERT INTO "department" ("id", "name")
SELECT nextval('department_id_seq'), 'Софтуерно инженерство'
WHERE NOT EXISTS (SELECT 1 FROM "department" WHERE "name" = 'Софтуерно инженерство');

INSERT INTO "department" ("id", "name")
SELECT nextval('department_id_seq'), 'Изкуствен интелект'
WHERE NOT EXISTS (SELECT 1 FROM "department" WHERE "name" = 'Изкуствен интелект');

INSERT INTO "department" ("id", "name")
SELECT nextval('department_id_seq'), 'Киберсигурност'
WHERE NOT EXISTS (SELECT 1 FROM "department" WHERE "name" = 'Киберсигурност');

-- Mandatory
INSERT INTO "subject" ("id", "name")
SELECT nextval('subject_id_seq'), 'Англииски език'
WHERE NOT EXISTS (SELECT 1 FROM "department" WHERE "name" = 'Англииски език');

INSERT INTO "subject" ("id", "name")
SELECT nextval('subject_id_seq'), 'Методи за изследване и развитие на дисертация'
WHERE NOT EXISTS (SELECT 1 FROM "department" WHERE "name" = 'Методи за изследване и развитие на дисертация');


INSERT INTO "supervisortype" ("id", "title")
SELECT nextval('supervisorType_id_seq'), 'доцент'
WHERE NOT EXISTS (SELECT 1 FROM "supervisortype" WHERE "title" = 'доцент');

INSERT INTO "supervisortype" ("id", "title")
SELECT nextval('supervisorType_id_seq'), 'асистент'
WHERE NOT EXISTS (SELECT 1 FROM "supervisortype" WHERE "title" = 'асистент');
