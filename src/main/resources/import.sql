-- NOTE: Faculty
INSERT INTO "faculty" ("id", "name")
SELECT nextval('faculty_id_seq'), 'Software engineering'
WHERE NOT EXISTS (SELECT 1 FROM "faculty" WHERE "name" = 'Software engineering');

INSERT INTO "faculty" ("id", "name")
SELECT nextval('faculty_id_seq'), 'Artificial inteligence'
WHERE NOT EXISTS (SELECT 1 FROM "faculty" WHERE "name" = 'Artificial inteligence');

INSERT INTO "faculty" ("id", "name")
SELECT nextval('faculty_id_seq'), 'Cybersecurity'
WHERE NOT EXISTS (SELECT 1 FROM "faculty" WHERE "name" = 'Cybersecurity');

INSERT INTO "faculty" ("id", "name")
  SELECT nextval('faculty_id_seq'), 'Automated information processing and management systems'
  WHERE NOT EXISTS (SELECT 1 FROM "faculty" WHERE "name" = 'Automated information processing and management systems');

-- NOTE: Supervisor
INSERT INTO "supervisortitle" ("id", "title")
  SELECT nextval('supervisortitle_id_seq'), 'professor'
    WHERE NOT EXISTS (SELECT 1 FROM "supervisortitle" WHERE "title" = 'professor');

INSERT INTO "supervisortitle" ("id", "title")
  SELECT nextval('supervisortitle_id_seq'), 'assistant'
    WHERE NOT EXISTS (SELECT 1 FROM "supervisortitle" WHERE "title" = 'assistant');

-- NOTE: Doc center role
INSERT INTO "doctoralcenterrole" ("id", "role")
  SELECT nextval('doctoralCenterRole_id_seq'), 'admin'
    WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'admin');

INSERT INTO "doctoralcenterrole" ("id", "role")
  SELECT nextval('doctoralCenterRole_id_seq'), 'expert'
    WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'expert');

INSERT INTO "doctoralcenterrole" ("id", "role")
  SELECT nextval('doctoralCenterRole_id_seq'), 'manager'
    WHERE NOT EXISTS (SELECT 1 FROM "doctoralcenterrole" WHERE "role" = 'manager');

-- NOTE: Doc Center
INSERT INTO "doctoralcenter" ("id", "oid", "name", "email", "picture", "role")
  SELECT
  nextval('doctoralCenter_id_seq'),
  'de6b478e-e4e4-422d-88fb-293fe69c6519',
  'admin',
  's19621609@onlineedu.tu-varna.bg',
  '',
  role.id
FROM "doctoralcenterrole" role
WHERE role.role = 'admin'
AND NOT EXISTS (
  SELECT 1
  FROM "doctoralcenter"
  WHERE "oid" = 'de6b478e-e4e4-422d-88fb-293fe69c6519'
);

-- TODO: Remove me for prod use
INSERT INTO "doctoralcenter" ("id", "oid", "name", "email", "picture", "role")
  SELECT
  nextval('doctoralCenter_id_seq'),
  '3d68809d-d92e-4a07-ac1e-30e5f2fe2a22',
  'Поддръжка ТУ Варна',
  't.teacher1@tu-varna.bg',
  '',
  role.id
  FROM "doctoralcenterrole" role
  WHERE role.role = 'expert'
  AND NOT EXISTS (
  SELECT 1
  FROM "doctoralcenter"
  WHERE "oid" = '3d68809d-d92e-4a07-ac1e-30e5f2fe2a22'
  );

INSERT INTO "committee" ("id", "oid", "name", "email", "role", "faculty")
  SELECT
  nextval('committee_id_seq'),
  '072c2bd9-75cd-49d7-a0d4-80a8494ebc6b',
  'Поддръжка ТУ Варна',
  't.teacher2@tu-varna.bg',
  role.id,
  faculty.id
FROM "committeerole" role
    JOIN "faculty" faculty ON faculty."name"='Software engineering'
WHERE role.role = 'chairman'
AND NOT EXISTS (
SELECT 1
FROM "committee"
WHERE "oid" = '072c2bd9-75cd-49d7-a0d4-80a8494ebc6b'
);


-- NOTE: Supervisor
INSERT INTO "supervisor" ("id", "oid", "name", "email", "title")
  SELECT
  nextval('supervisor_id_seq'),
  '56ef9437-2706-4d78-9ea5-f6ba18ca1ef7',
  'Поддръжка ТУ Варна',
  't.teacher3@tu-varna.bg',
  title.id
 FROM "supervisortitle" title
 WHERE title.title = 'professor'
 AND NOT EXISTS (
    SELECT 1
    FROM "supervisor"
    WHERE "oid" = '56ef9437-2706-4d78-9ea5-f6ba18ca1ef7'
 );

-- NOTE: Candidate
INSERT INTO "candidate" ("id", "name", "email", "country", "city","address", "biography", "pin", "status")
  SELECT
  nextval('candidate_id_seq'),
  'Явор Яворов',
  'ivangeorgiev12133@gmail.com',
  'България',
  'Варна',
  'улица Мир 3',
  'f47b5dccad4bec91c5c8f7dab4145c38056e2a56032c3d9eac0403c31ba0deae',
  '1111111111',
  'waiting';

-- NOTE: Phd status
INSERT INTO "phdstatus" ("id", "status")
SELECT nextval('PhdStatus_id_seq'), 'enrolled'
WHERE NOT EXISTS (SELECT 1 FROM "phdstatus" WHERE "status" = 'enrolled');

INSERT INTO "phdstatus" ("id", "status")
SELECT nextval('PhdStatus_id_seq'), 'graduated'
WHERE NOT EXISTS (SELECT 1 FROM "phdstatus" WHERE "status" = 'graduated');

INSERT INTO "phdstatus" ("id", "status")
SELECT nextval('PhdStatus_id_seq'), 'terminated'
WHERE NOT EXISTS (SELECT 1 FROM "phdstatus" WHERE "status" = 'terminated');

-- NOTE: Committee role
INSERT INTO "committeerole" ("id", "role")
SELECT nextval('committeeRole_id_seq'), 'chairman'
WHERE NOT EXISTS (SELECT 1 FROM "committeerole" WHERE "role" = 'chairman');

INSERT INTO "committeerole" ("id", "role")
SELECT nextval('committeeRole_id_seq'), 'member'
WHERE NOT EXISTS (SELECT 1 FROM "committeerole" WHERE "role" = 'member');

-- NOTE: Mode
INSERT INTO "mode" ("id", "mode")
SELECT nextval('mode_id_seq'), 'regular'
WHERE NOT EXISTS (SELECT 1 FROM "mode" WHERE "mode" = 'regular');

INSERT INTO "mode" ("id", "mode")
SELECT nextval('mode_id_seq'), 'part_time'
WHERE NOT EXISTS (SELECT 1 FROM "mode" WHERE "mode" = 'part_time');

-- NOTE: Curriculum
INSERT INTO "curriculum" ("id", "description", "yearperiod", "mode", "faculty")
  SELECT nextval('curriculum_id_seq'), 'Automated information processing and management systems', 4, mode.id, faculty.id
  FROM "mode" mode
    JOIN "faculty" faculty ON faculty."name"='Software engineering'
  WHERE mode.mode = 'part_time'
    AND NOT EXISTS (SELECT 1 FROM "curriculum" WHERE "description" = 'Automated information processing and management systems'
  );

INSERT INTO "curriculum" ("id", "description", "yearperiod", "mode", "faculty")
  SELECT nextval('curriculum_id_seq'), 'Automated information processing and management systems', 3, mode.id, faculty.id
  FROM "mode" mode
    JOIN "faculty" faculty ON faculty."name"='Software engineering'
  WHERE mode.mode = 'regular'
    AND NOT EXISTS (SELECT 1 FROM "curriculum" WHERE "description" = 'Automated information processing and management systems'
  );

-- NOTE: Subjects
-- Mandatory
INSERT INTO "subject" ("id", "name")
  SELECT nextval('subject_id_seq'), 'English'
    WHERE NOT EXISTS (SELECT 1 FROM "faculty" WHERE "name" = 'English');

INSERT INTO "subject" ("id", "name")
  SELECT nextval('subject_id_seq'), 'Methods of Research and Development of dissertation'
    WHERE NOT EXISTS (SELECT 1 FROM "subject" WHERE "name" = 'Methods of Research and Development of dissertation');

INSERT INTO "subject" ("id", "name")
  SELECT nextval('subject_id_seq'), 'Block C (PhD minimum)'
    WHERE NOT EXISTS (SELECT 1 FROM "subject" WHERE "name" = 'Block C (PhD minimum)');

-- Optional
INSERT INTO "subject" ("id", "name")
	SELECT nextval('subject_id_seq'), 'Cryptography and data protection'
		WHERE NOT EXISTS (SELECT 1 FROM "subject" WHERE "name" = 'Cryptography and data protection');

INSERT INTO "subject" ("id", "name")
	SELECT nextval('subject_id_seq'), 'Processing of visual information'
		WHERE NOT EXISTS (SELECT 1 FROM "subject" WHERE "name" = 'Processing of visual information');

INSERT INTO "subject" ("id", "name")
	SELECT nextval('subject_id_seq'), 'Programming technologies on the Internet'
		WHERE NOT EXISTS (SELECT 1 FROM "subject" WHERE "name" = 'Programming technologies on the Internet');

INSERT INTO "subject" ("id", "name")
	SELECT nextval('subject_id_seq'), 'Multimedia systems and technologies'
		WHERE NOT EXISTS (SELECT 1 FROM "subject" WHERE "name" = 'Multimedia systems and technologies');

INSERT INTO "subject" ("id", "name")
	SELECT nextval('subject_id_seq'), 'Modern Software Technologies'
		WHERE NOT EXISTS (SELECT 1 FROM "subject" WHERE "name" = 'Modern Software Technologies');

INSERT INTO "subject" ("id", "name")
	SELECT nextval('subject_id_seq'), 'Computer networks'
		WHERE NOT EXISTS (SELECT 1 FROM "subject" WHERE "name" = 'Computer networks');

INSERT INTO "subject" ("id", "name")
	SELECT nextval('subject_id_seq'), 'Programming in Mathlab/ C#/ Java/ Python or other language'
		WHERE NOT EXISTS (SELECT 1 FROM "subject" WHERE "name" = 'Programming in Mathlab/ C#/ Java/ Python or other language');

INSERT INTO "subject" ("id", "name")
	SELECT nextval('subject_id_seq'), 'Databases and Information Technology'
		WHERE NOT EXISTS (SELECT 1 FROM "subject" WHERE "name" = 'Databases and Information Technology');

INSERT INTO "subject" ("id", "name")
	SELECT nextval('subject_id_seq'), 'Machine learning'
		WHERE NOT EXISTS (SELECT 1 FROM "subject" WHERE "name" = 'Machine learning');

INSERT INTO "subject" ("id", "name")
	SELECT nextval('subject_id_seq'), 'Bioinformatics'
		WHERE NOT EXISTS (SELECT 1 FROM "subject" WHERE "name" = 'Bioinformatics');

-- NOTE: PHD
INSERT INTO "phd" ("id", "oid", "name", "email", "pin", "status")
SELECT
  nextval('phd_id_seq'),
  'def80d65-6faf-4658-9ebf-d92bb9dd5179',
  'ИЛИЯН КИРИЛОВ КОСТОВ СИ_3 1к',
  's23651224@onlineedu.tu-varna.bg',
  '1111111111',
  status.id
FROM "phdstatus" status
WHERE status.status = 'enrolled'
AND NOT EXISTS (
    SELECT 1
    FROM "phd"
    WHERE "oid" = 'def80d65-6faf-4658-9ebf-d92bb9dd5179'
);

-- NOTE: Committee
INSERT INTO "committee" ("id", "oid", "name", "email", "role", "faculty")
  SELECT
  nextval('committee_id_seq'),
  'f4bcb028-9589-4ecb-ac8b-4846c68ba123',
  'Поддръжка ТУ Варна',
  't.teacher4@tu-varna.bg',
  role.id,
  faculty.id
FROM "committeerole" role
    JOIN "faculty" faculty ON faculty."name"='Software engineering'
WHERE role.role = 'member'
AND NOT EXISTS (
SELECT 1
FROM "committee"
WHERE "oid" = 'f4bcb028-9589-4ecb-ac8b-4846c68ba123'
);

INSERT INTO "committee" ("id", "oid", "name", "email", "role", "faculty")
  SELECT
  nextval('committee_id_seq'),
  'bc43721c-c402-4f85-8d48-d40cc16d6ed3',
  'Поддръжка ТУ Варна',
  't.teacher5@tu-varna.bg',
  role.id,
  faculty.id
FROM "committeerole" role
    JOIN "faculty" faculty ON faculty."name"='Software engineering'
WHERE role.role = 'member'
AND NOT EXISTS (
SELECT 1
FROM "committee"
WHERE "oid" = 'bc43721c-c402-4f85-8d48-d40cc16d6ed3'
);

INSERT INTO "committee" ("id", "oid", "name", "email", "role", "faculty")
  SELECT
  nextval('committee_id_seq'),
  'a6d47203-6737-4250-b4b7-3ab097f0d298',
  'Поддръжка ТУ Варна',
  't.teacher6@tu-varna.bg',
  role.id,
  faculty.id
FROM "committeerole" role
    JOIN "faculty" faculty ON faculty."name"='Software engineering'
WHERE role.role = 'member'
AND NOT EXISTS (
SELECT 1
FROM "committee"
WHERE "oid" = 'a6d47203-6737-4250-b4b7-3ab097f0d298'
);

-- -- NOTE: Commision
-- INSERT INTO "commision" ("id", "members")
--   SELECT
--   nextval('commission_id_seq'),
--   {1,2,3,4};
