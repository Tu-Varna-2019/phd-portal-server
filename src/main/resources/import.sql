-- NOTE: Faculty
INSERT INTO "faculty" ("id", "name")
  VALUES
    (
     1, 'Software engineering'
    ),
    (
     2, 'Artificial inteligence'
    ),
    (
     3, 'Cybersecurity'
    );

-- NOTE: SupervisorTitle
INSERT INTO "supervisortitle" ("id", "title")
  VALUES
    (
     1, 'professor'
    ),
    (
     2, 'assistant'
    );

-- NOTE: Doc center role
INSERT INTO "doctoralcenterrole" ("id", "role")
  VALUES
    (
     1, 'admin'
    ),
    (
     2, 'expert'
    ),
    (
     3, 'manager'
    );

-- NOTE: Doc Center
INSERT INTO "doctoralcenter" ("id", "oid", "name", "email", "picture", "role")
  VALUES
    (
      1,
      'de6b478e-e4e4-422d-88fb-293fe69c6519',
      'Администратор',
      's19621609@onlineedu.tu-varna.bg',
      '',
      1
    ),
    (
      2,
      '3d68809d-d92e-4a07-ac1e-30e5f2fe2a22',
      'Поддръжка ТУ Варна',
      't.teacher1@tu-varna.bg',
      '',
      2
    );

-- NOTE: Supervisor
INSERT INTO "supervisor" ("id", "oid", "name", "email", "title")
  VALUES
    (
      1,
      '56ef9437-2706-4d78-9ea5-f6ba18ca1ef7',
      'Поддръжка ТУ Варна',
      't.teacher3@tu-varna.bg',
      1
    );

-- NOTE: Candidate
INSERT INTO "candidate" ("id", "name", "email", "country", "city","address", "biography", "pin", "status")
  VALUES
    (
      1,
      'Явор Яворов',
      'ivangeorgiev12133@gmail.com',
      'България',
      'Варна',
      'улица Мир 3',
      'f47b5dccad4bec91c5c8f7dab4145c38056e2a56032c3d9eac0403c31ba0deae',
      '1111111111',
      'waiting'
    );

-- NOTE: Unauthorized Users
INSERT INTO "unauthorizedusers" ("id", "oid", "name", "email", "timestamp","isallowed")
  VALUES
    (
      1,
      'c3e9208f-922e-4dbd-95b0-f9b76350d668',
      'Velislav Kolesnichenko',
      'vkolesnichenko@tu-varna.bg',
      '2025-02-04T16:15:50Z',
      false
    );

-- NOTE: Phd status
INSERT INTO "phdstatus" ("id", "status")
  VALUES
    (
      1, 'enrolled'
    ),
    (
      2, 'graduated'
    ),
    (
      3, 'terminated'
    );

-- NOTE: Committee role
INSERT INTO "committeerole" ("id", "role")
  VALUES
    (
      1, 'chairman'
    ),
    (
      2, 'member'
    );

-- NOTE: Mode
INSERT INTO "mode" ("id", "mode")
  VALUES
    (
      1, 'regular'
    ),
    (
      2, 'part_time'
    );

-- NOTE: Curriculum
INSERT INTO "curriculum" ("id", "description", "yearperiod", "mode", "faculty")
  VALUES
    (
      1, 'Automated information processing and management systems', 4, 1, 1
    ),
    (
      2, 'Automated information processing and management systems', 4, 2, 1
    );


-- NOTE: Subjects
-- Mandatory
INSERT INTO "subject" ("id", "name")
  VALUES
    (
      -- Mandatory
      1, 'English'
    ),
    (
      2, 'Methods of Research and Development of dissertation'
    ),
    (
      3, 'Block C (PhD minimum)'
    ),
    (
      -- Optional
      4, 'Cryptography and data protection'
    ),
    (
      5, 'Processing of visual information'
    ),
    (
      6, 'Programming technologies on the Internet'
    ),
    (
      7, 'Multimedia systems and technologies'
    ),
    (
      8, 'Modern Software Technologies'
    ),
    (
      9, 'Programming in Mathlab/ C#/ Java/ Python or other language'
    ),
    (
      10, 'Databases and Information Technology'
    ),
    (
      11, 'Machine learning'
    ),
    (
      12, 'Bioinformatics'
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

-- NOTE: PHD
INSERT INTO "phd" ("id", "oid", "name", "email", "pin", "status")
  VALUES
    (
      1,
      'def80d65-6faf-4658-9ebf-d92bb9dd5179',
      'ИЛИЯН КИРИЛОВ КОСТОВ СИ_3 1к',
      's23651224@onlineedu.tu-varna.bg',
      '1111111111',
      1
    );

-- NOTE: Committee
INSERT INTO "committee" ("id", "oid", "name", "email", "role", "faculty")
  VALUES
  (
    1,
    '072c2bd9-75cd-49d7-a0d4-80a8494ebc6b',
    'Поддръжка ТУ Варна',
    't.teacher2@tu-varna.bg',
    1,
    1
  ),
  (
    2,
    'f4bcb028-9589-4ecb-ac8b-4846c68ba123',
    'Поддръжка ТУ Варна',
    't.teacher4@tu-varna.bg',
    2,
    1
  ),
  (
    3,
    'bc43721c-c402-4f85-8d48-d40cc16d6ed3',
    'Поддръжка ТУ Варна',
    't.teacher5@tu-varna.bg',
    2,
    1
  ),
  (
    4,
    'a6d47203-6737-4250-b4b7-3ab097f0d298',
    'Поддръжка ТУ Варна',
    't.teacher6@tu-varna.bg',
    2,
    1
  );

-- -- NOTE: Commision
-- INSERT INTO "commision" ("id", "members")
--   VALUES
--   nextval('commission_id_seq'),
--   {1,2,3,4};
