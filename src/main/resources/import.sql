INSERT INTO "doctoralcenterrole" ("id", "role")
SELECT nextval('doctoralCenterRole_id_seq'), 'expert'
WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'expert');

INSERT INTO "doctoralcenterrole" ("id", "role")
SELECT nextval('doctoralCenterRole_id_seq'), 'manager'
WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'manager');


INSERT INTO "statusphd" ("id", "status")
SELECT nextval('statusPhd_id_seq'), 'enrolled'
WHERE NOT EXISTS (SELECT 1 FROM "statusphd" WHERE "status" = 'enrolled');

INSERT INTO "statusphd" ("id", "status")
SELECT nextval('statusPhd_id_seq'), 'graduated'
WHERE NOT EXISTS (SELECT 1 FROM "statusphd" WHERE "status" = 'graduated');

INSERT INTO "statusphd" ("id", "status")
SELECT nextval('statusPhd_id_seq'), 'terminated'
WHERE NOT EXISTS (SELECT 1 FROM "statusphd" WHERE "status" = 'terminated');
