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
  name         TEXT,
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
  hasOnBoardGPU BOOLEAN,
  socket        CPUsockets,
  formFactor    FormFactors,
  ramType       RAMTypes,
  busSpeed      INTEGER
);

CREATE TABLE ComputerCases (
  id         INTEGER PRIMARY KEY REFERENCES Components (id),
  formFactor FormFactors
);

/* TODO add check */
CREATE TABLE ComputerSystems (
  id           INTEGER PRIMARY KEY,
  name         TEXT,
  cpu          INTEGER REFERENCES CPUs (id),
  mainboard    INTEGER REFERENCES Mainboards (id),
  gpu          INTEGER REFERENCES GPUs (id),
  ram          INTEGER REFERENCES RAMs (id),
  computerCase INTEGER REFERENCES ComputerCases (id)
);

