-- NOTE: Faculty
INSERT INTO "faculty" ("id", "name")
  VALUES
    (
      nextval('faculty_id_seq'),
     'Software engineering'
    ),
    (
      nextval('faculty_id_seq'),
      'Artificial inteligence'
    ),
    (
      nextval('faculty_id_seq'),
      'Cybersecurity'
    );


-- NOTE: Doc center role
INSERT INTO "doctoral_center_role" ("id", "role")
  VALUES
    (
      nextval('doctoralCenterRole_id_seq'),
      'admin'
    ),
    (
      nextval('doctoralCenterRole_id_seq'),
      'expert'
    ),
    (
      nextval('doctoralCenterRole_id_seq'),
      'manager'
    );

-- NOTE: Doc Center
INSERT INTO "doctoral_center" ("id", "oid", "name", "email", "picture", "role")
  VALUES
    (
      nextval('doctoralCenter_id_seq'),
      'de6b478e-e4e4-422d-88fb-293fe69c6519',
      'Администратор',
      's19621609@onlineedu.tu-varna.bg',
      '',
      (SELECT id FROM "doctoral_center_role" WHERE role = 'admin')
    ),
    (
      nextval('doctoralCenter_id_seq'),
      '3d68809d-d92e-4a07-ac1e-30e5f2fe2a22',
      'Поддръжка ТУ Варна',
      't.teacher1@tu-varna.bg',
      '',
      (SELECT id FROM "doctoral_center_role" WHERE role = 'expert')
    );

-- NOTE: Candidate Status
INSERT INTO "candidate_status" ("id", "status")
  VALUES
    (
      nextval('candidatestatus_id_seq'),
      'waiting'
    ),
    (
      nextval('candidatestatus_id_seq'),
      'approved'
    ),
    (
      nextval('candidatestatus_id_seq'),
      'rejected'
    ),
    (
      nextval('candidatestatus_id_seq'),
      'reviewing'
    );

-- NOTE: Unauthorized Users
INSERT INTO "unauthorized" ("id", "oid", "name", "email", "timestamp","allowed")
  VALUES
    (
      nextval('unauthorizedUsers_id_seq'),
      'c3e9208f-922e-4dbd-95b0-f9b76350d668',
      'Velislav Kolesnichenko',
      'vkolesnichenko@tu-varna.bg',
      '2025-02-04T16:15:50Z',
      false
    );

-- NOTE: Phd status
INSERT INTO "phd_status" ("id", "status")
  VALUES
    (
      nextval('unauthorizedUsers_id_seq'),
      'enrolled'
    ),
    (
      nextval('unauthorizedUsers_id_seq'),
      'graduated'
    ),
    (
      nextval('unauthorizedUsers_id_seq'),
      'terminated'
    );

-- NOTE: Committee role
INSERT INTO "committee_role" ("id", "role")
  VALUES
    (
      nextval('committeeRole_id_seq'),
      'chairman'
    ),
    (
      nextval('committeeRole_id_seq'),
      'member'
    );

-- NOTE: CommitteeTitle
INSERT INTO "committee_title" ("id", "title")
  VALUES
    (
      nextval('committeetitle_id_seq'),
      'professor'
    ),
    (
      nextval('committeetitle_id_seq'),
      'assistant'
    );

-- NOTE: Mode
INSERT INTO "mode" ("id", "mode", "year_period")
  VALUES
    (
      nextval('mode_id_seq'),
      'regular', 3
    ),
    (
      nextval('mode_id_seq'),
      'part_time', 4
    );

-- NOTE: Curriculum
INSERT INTO "curriculum" ("id", "name","is_public", "mode", "faculty")
  VALUES
    (
      1,
      'Automated information processing and management systems',
      true,
      (SELECT id FROM "mode" WHERE mode = 'regular'),
      (SELECT id FROM "faculty" WHERE name = 'Software engineering')

    ),
    (
      2,
      'Automated information processing and management systems',
      true,
      (SELECT id FROM "mode" WHERE mode = 'part_time'),
      (SELECT id FROM "faculty" WHERE name = 'Software engineering')
    );

-- NOTE: Candidate
INSERT INTO "candidate" ("id", "name", "email", "country", "city", "address", "post_code", "biography", "pin", "year_accepted", "status","exam_step", "curriculum", "faculty")
  VALUES
    (
      nextval('candidate_id_seq'),
      'Явор Яворов',
      'ivangeorgiev12133@gmail.com',
      'България',
      'Варна',
      'улица Мир 3',
      '9000',
      'f47b5dccad4bec91c5c8f7dab4145c38056e2a56032c3d9eac0403c31ba0deae',
      '1111111112',
      2025,
      (SELECT cs.id FROM "candidate_status" cs WHERE status = 'waiting'),
      1,
      (SELECT c.id FROM "curriculum" c JOIN mode m ON (c.mode=m.id) WHERE c.name = 'Automated information processing and management systems' AND m.mode = 'regular' ),
      (SELECT f.id FROM "faculty" f WHERE name = 'Software engineering')
    );


-- NOTE: Phd
INSERT INTO "phd" ("id", "oid", "name", "email", "pin","post_code","picture", "status")
  VALUES
    (
       nextval('phd_id_seq'),
      'def80d65-6faf-4658-9ebf-d92bb9dd5179',
      'ИЛИЯН КИРИЛОВ КОСТОВ СИ_3 1к',
      's23651224@onlineedu.tu-varna.bg',
      '1111111111',
      '9000',
      '',
      (SELECT id FROM "phd_status" WHERE status = 'enrolled')
    );

-- NOTE: Committee
INSERT INTO "committee" ("id", "oid", "name", "email", "picture", "title", "role", "faculty")
  VALUES
  (
    nextval('committee_id_seq'),
    '072c2bd9-75cd-49d7-a0d4-80a8494ebc6b',
    'Поддръжка ТУ Варна',
    't.teacher2@tu-varna.bg',
    '',
    (SELECT id FROM "committee_title" WHERE title = 'professor'),
    (SELECT id FROM "committee_role" WHERE role = 'chairman'),
    (SELECT id FROM "faculty" WHERE name = 'Software engineering')
  ),
  (
    nextval('committee_id_seq'),
    'f4bcb028-9589-4ecb-ac8b-4846c68ba123',
    'Поддръжка ТУ Варна',
    't.teacher4@tu-varna.bg',
    '',
    (SELECT id FROM "committee_title" WHERE title = 'assistant'),
    (SELECT id FROM "committee_role" WHERE role = 'member'),
    (SELECT id FROM "faculty" WHERE name = 'Software engineering')
  ),
  (
    nextval('committee_id_seq'),
    'bc43721c-c402-4f85-8d48-d40cc16d6ed3',
    'Поддръжка ТУ Варна',
    't.teacher5@tu-varna.bg',
    '',
    (SELECT id FROM "committee_title" WHERE title = 'assistant'),
    (SELECT id FROM "committee_role" WHERE role = 'member'),
    (SELECT id FROM "faculty" WHERE name = 'Software engineering')
  ),
  (
    nextval('committee_id_seq'),
    'a6d47203-6737-4250-b4b7-3ab097f0d298',
    'Поддръжка ТУ Варна',
    't.teacher6@tu-varna.bg',
    '',
    (SELECT id FROM "committee_title" WHERE title = 'professor'),
    (SELECT id FROM "committee_role" WHERE role = 'member'),
    (SELECT id FROM "faculty" WHERE name = 'Software engineering')
  );

-- -- NOTE: Commision
-- INSERT INTO "commission" ("id", "members")
--   VALUES
--   nextval('commission_id_seq'),
--   {1,2,3,4};

-- NOTE: Subjects
-- Mandatory
INSERT INTO "subject" ("id", "name", "semester", "course", "teacher")
  VALUES
    (
      -- Mandatory
      1, 'English', 1, 1, 3
    ),
    (
      2, 'Methods of Research and Development of dissertation', 1, 1, 1
    ),
    (
      3, 'Block C (PhD minimum)', 1, 1, 3
    ),
    (
      -- Optional
      4, 'Cryptography and data protection', 2, 1, 1
    ),
    (
      5, 'Processing of visual information', 2, 1, 3
    ),
    (
      6, 'Programming technologies on the Internet', 2, 1, 2
    ),
    (
      7, 'Multimedia systems and technologies', 1, 2, 2
    ),
    (
      8, 'Modern Software Technologies', 1, 2, 3
    ),
    (
      9, 'Programming in Mathlab/ C#/ Java/ Python or other language', 1, 2, 1
    ),
    (
      10, 'Databases and Information Technology', 2, 2, 2
    ),
    (
      11, 'Machine learning', 2, 2, 3
    ),
    (
      12, 'Bioinformatics', 2, 2, 1
    ),
    (
    -- NOTE: Needed for the mandatory exams
      13, 'Automated information processing and management systems', 1, 1, 2
    );

-- NOTE: Curriculum - Subjects
INSERT INTO "curriculum_subject" ("curriculum_id", "subject_id")
  VALUES
   (1, 1),
   (1, 2),
   (1, 3),
   (1, 4),
   (1, 5),
   (1, 6),
   (1, 7),
   (1, 8),
   (1, 9),
   (1, 10),
   (1, 11),
   (1, 12),
   (2, 1),
   (2, 2),
   (2, 3),
   (2, 4),
   (2, 5),
   (2, 6),
   (2, 7),
   (2, 8),
   (2, 9),
   (2, 10),
   (2, 11),
   (2, 12);
