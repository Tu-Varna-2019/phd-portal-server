INSERT INTO "doctoralcenterrole" ("id", "role")
SELECT nextval('doctoralCenterRole_id_seq'), 'admin'
WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'admin');

INSERT INTO "doctoralcenterrole" ("id", "role")
SELECT nextval('doctoralCenterRole_id_seq'), 'expert'
WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'expert');

INSERT INTO "doctoralcenterrole" ("id", "role")
SELECT nextval('doctoralCenterRole_id_seq'), 'manager'
WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'manager');

INSERT INTO "doctoralcenter" ("id", "oid", "name", "email", "picture", "role")
				SELECT
								nextval('doctoralCenter_id_seq'),
								'de6b478e-e4e4-422d-88fb-293fe69c6519',
								'admin',
								's19621609@onlineedu.tu-varna.bg',
								'doctoralCenter_image.png',
								role.id
				FROM "doctoralcenterrole" role
				WHERE role.role = 'admin'
				AND NOT EXISTS (
								SELECT 1
								FROM "doctoralcenter"
								WHERE "oid" = 'de6b478e-e4e4-422d-88fb-293fe69c6519');

INSERT INTO "statusphd" ("id", "status")
SELECT nextval('statusPhd_id_seq'), 'enrolled'
WHERE NOT EXISTS (SELECT 1 FROM "statusphd" WHERE "status" = 'enrolled');

INSERT INTO "statusphd" ("id", "status")
SELECT nextval('statusPhd_id_seq'), 'graduated'
WHERE NOT EXISTS (SELECT 1 FROM "statusphd" WHERE "status" = 'graduated');

INSERT INTO "statusphd" ("id", "status")
SELECT nextval('statusPhd_id_seq'), 'terminated'
WHERE NOT EXISTS (SELECT 1 FROM "statusphd" WHERE "status" = 'terminated');


INSERT INTO "committeetype" ("id", "type")
SELECT nextval('committeeType_id_seq'), 'chairman'
WHERE NOT EXISTS (SELECT 1 FROM "committeetype" WHERE "type" = 'chairman');

INSERT INTO "committeetype" ("id", "type")
SELECT nextval('committeeType_id_seq'), 'member'
WHERE NOT EXISTS (SELECT 1 FROM "committeetype" WHERE "type" = 'member');


INSERT INTO "mode" ("id", "mode")
SELECT nextval('mode_id_seq'), 'regular'
WHERE NOT EXISTS (SELECT 1 FROM "mode" WHERE "mode" = 'regular');

INSERT INTO "mode" ("id", "mode")
SELECT nextval('mode_id_seq'), 'part_time'
WHERE NOT EXISTS (SELECT 1 FROM "mode" WHERE "mode" = 'part_time');


INSERT INTO "department" ("id", "name")
SELECT nextval('department_id_seq'), 'Software engineering'
WHERE NOT EXISTS (SELECT 1 FROM "department" WHERE "name" = 'Software engineering');

INSERT INTO "department" ("id", "name")
SELECT nextval('department_id_seq'), 'Artificial inteligence'
WHERE NOT EXISTS (SELECT 1 FROM "department" WHERE "name" = 'Artificial inteligence');

INSERT INTO "department" ("id", "name")
SELECT nextval('department_id_seq'), 'Cybersecurity'
WHERE NOT EXISTS (SELECT 1 FROM "department" WHERE "name" = 'Cybersecurity');

-- Mandatory
INSERT INTO "subject" ("id", "name")
SELECT nextval('subject_id_seq'), 'English'
WHERE NOT EXISTS (SELECT 1 FROM "department" WHERE "name" = 'English');

INSERT INTO "subject" ("id", "name")
SELECT nextval('subject_id_seq'), 'Methods of Research and Development of dissertation'
WHERE NOT EXISTS (SELECT 1 FROM "department" WHERE "name" = 'Methods of Research and Development of dissertation');


INSERT INTO "supervisortype" ("id", "title")
SELECT nextval('supervisorType_id_seq'), 'professor'
WHERE NOT EXISTS (SELECT 1 FROM "supervisortype" WHERE "title" = 'professor');

INSERT INTO "supervisortype" ("id", "title")
SELECT nextval('supervisorType_id_seq'), 'assistant'
WHERE NOT EXISTS (SELECT 1 FROM "supervisortype" WHERE "title" = 'assistant');
