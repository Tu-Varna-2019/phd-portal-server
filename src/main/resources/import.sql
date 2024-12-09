INSERT INTO "doctoralcenterrole" ("id", "role")
SELECT nextval('doctoralCenterRole_id_seq'), 'expert'
WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'expert');

INSERT INTO "doctoralcenterrole" ("id", "role")
SELECT nextval('doctoralCenterRole_id_seq'), 'manager'
WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'manager');
