-- noinspection SqlNoDataSourceInspectionForFile

/* Enumerated types */
CREATE TYPE CPUSockets AS ENUM (
  'LGA1150',
  'LGA1151',
  'AM3',
  'AM4'
);

CREATE TYPE RAMTypes AS ENUM (
  'DDR3',
  'DDR4'
);

CREATE TYPE FormFactors AS ENUM (
  'mATX',
  'mITX',
  'ATX'
);

/* Super table that stores generic components */
CREATE TABLE Components (
  id           INTEGER PRIMARY KEY,
  componentName         TEXT,
  price        REAL,
  minStock     INTEGER,
  prefStock    INTEGER,
  currentStock INTEGER
);

/* The following tables inherits from Component */
CREATE TABLE CPUs (
  id       INTEGER PRIMARY KEY REFERENCES Components (id),
  busSpeed INTEGER,
  socket   CPUsockets
);

CREATE TABLE RAMs (
  id       INTEGER PRIMARY KEY REFERENCES Components (id),
  busSpeed INTEGER,
  ramType  RAMTypes
);

CREATE TABLE GPUs (
  id INTEGER PRIMARY KEY REFERENCES Components (id)
);

CREATE TABLE Mainboards (
  id            INTEGER PRIMARY KEY REFERENCES Components (id),
  onBoardGPU BOOLEAN,
  socket        CPUsockets,
  formFactor    FormFactors,
  ramType       RAMTypes
);

CREATE TABLE ComputerCases (
  id         INTEGER PRIMARY KEY REFERENCES Components (id),
  formFactor FormFactors
);

/* TODO add check */
CREATE TABLE ComputerSystems (
  id           INTEGER PRIMARY KEY,
  computerSystemName         TEXT,
  cpu          INTEGER REFERENCES CPUs (id)          NOT NULL,
  mainboard    INTEGER REFERENCES Mainboards (id)    NOT NULL,
  gpu          INTEGER REFERENCES GPUs (id),
  ram          INTEGER REFERENCES RAMs (id)          NOT NULL,
  computerCase INTEGER REFERENCES ComputerCases (id) NOT NULL
);

CREATE OR REPLACE FUNCTION checkInsertOnComputerSystem()
  RETURNS TRIGGER AS $$
BEGIN
  IF ((SELECT busSpeed
       FROM CPUs
       WHERE id = new.cpu) <> (SELECT busSpeed
                               FROM RAMs
                               WHERE id = new.ram))
  THEN
    RAISE EXCEPTION 'CPU and RAM bus speed must match';
  END IF;
  IF ((SELECT ramType
       FROM RAMs
       WHERE id = new.ram) <> (SELECT ramType
                               FROM Mainboards
                               WHERE id = new.mainboard))
  THEN
    RAISE EXCEPTION 'RAM type must match with motherboard';
  END IF;
  IF ((SELECT formFactor
       FROM ComputerCases
       WHERE id = new.computerCase) <> (SELECT formFactor
                                        FROM Mainboards
                                        WHERE id = new.mainboard))
  THEN
    RAISE EXCEPTION 'Case form factor and motherboard form factor must match';
  END IF;
  IF ((SELECT socket
       FROM CPUs
       WHERE id = new.cpu) <> (SELECT socket
                               FROM Mainboards
                               WHERE id = new.mainboard))
  THEN
    RAISE EXCEPTION 'CPU socket and motherboard socket must match ';
  END IF;
  IF (new.gpu IS NULL AND (SELECT onBoardGPU
                           FROM Mainboards
                           WHERE id = new.mainboard) = FALSE)
  THEN
    RAISE EXCEPTION 'A Computer System must have a gpu or a motherboard that has on board graphics';
  END IF;
  RETURN new;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER checkInsertOnComputerSystemTrigger
BEFORE INSERT ON ComputerSystems
FOR EACH ROW EXECUTE PROCEDURE checkInsertOnComputerSystem();
