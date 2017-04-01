-- noinspection SqlResolveForFile

-- noinspection SqlNoDataSourceInspectionForFile

INSERT INTO Components (id, name, price, minStock, prefStock, currentStock) VALUES

  -- RAM
  (640, 'HyperX Fury DDR4 2133MHz 16GB', 1279, 5, 20, 7),
  (838, 'Corsair Vengeance DDR3 1600MHz 8GB', 629, 5, 20, 15),
  (368, 'Corsair Vengeance LED DDR4 3200MHz 16GB', 1399, 5, 20, 3),

  -- CPU
  (973, 'Intel Core i7-7700K Kaby Lake Processor', 2874, 3, 15, 12),
  (524, 'Intel Core i5-4460 Processor', 1499, 3, 15, 6),
  (433, 'AMD Ryzen 7 1700 Processor', 2929, 3, 15, 2),

  -- GPU
  (720, 'Sapphire Radeon RX 480 8GB NITRO+', 2249, 15, 40, 23),

  -- Mainboard
  (749, 'ASUS Prime Z270-P, Socket-1151', 998, 4, 10, 8),
  (336, 'ASUS B85M-G, Socket-1150', 599, 4, 10, 2),
  (572, 'ASUS ROG Crosshair VI Hero, Socket-AM4', 2290, 4, 10, 6),

  -- Cases
  (228, 'Corsair Carbide Clear 400C Midi Tower', 769, 7, 30, 22),
  (402, 'Fractal Define Nano S Black Window', 599, 7, 30, 8),
  (436, 'NZXT H440 New Edition Silent Ultra', 995, 7, 30, 2);

INSERT INTO RAMs (id, busSpeed, ramType) VALUES
  (640, 2133, 'DDR4'),
  (838, 1600, 'DDR3'),
  (368, 3200, 'DDR4');

INSERT INTO CPUs (id, busSpeed, socket) VALUES
  (973, 2133, 'LGA1151'),
  (524, 1600, 'LGA1150'),
  (433, 3200, 'AM4');

INSERT INTO GPUs (id) VALUES
  (720);

INSERT INTO ComputerCases (id, formFactor) VALUES
  (228, 'ATX'),
  (402, 'mATX'),
  (436, 'ATX');

INSERT INTO Mainboards (id, hasOnBoardGPU, socket, formFactor, ramType) VALUES
  (749, TRUE, 'LGA1151', 'ATX', 'DDR4'),
  (336, TRUE, 'LGA1150', 'mATX', 'DDR3'),
  (572, FALSE, 'AM4', 'ATX', 'DDR4');

INSERT INTO ComputerSystems (id, name, cpu, mainboard, gpu, ram, computerCase) VALUES
  (688, 'Super Fast Build', 973, 749, NULL, 640, 228),
  (293, 'Coolmaster masterComputer', 524, 336, NULL, 838, 402),
  (375, 'AMD Ryzen Rig', 433, 572, 720, 368, 436);