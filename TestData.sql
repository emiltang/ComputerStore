-- noinspection SqlResolveForFile

-- noinspection SqlNoDataSourceInspectionForFile

INSERT INTO Components (id, componentName, price, minStock, prefStock, currentStock) VALUES

  -- RAM
  (640, 'HyperX Fury DDR4 2133MHz 16GB', 1279, 5, 20, 7),
  (838, 'Corsair Vengeance DDR3 1600MHz 8GB', 629, 5, 20, 15),
  (368, 'Corsair Vengeance LED DDR4 3200MHz 16GB', 1399, 5, 20, 3),
  (900, 'Crucial DDR4 2133MHz 16GB', 1139, 5, 20, 0),
  (605, 'Kingston DDR3 1333MHz 16GB', 1099, 5, 20, 10),
  (182, 'Crucial DDR4 2400MHz 256GB', 30099, 5, 20, 2),
  (254, 'G.Skill Trident Z RGB 3200MHz 32GB', 4099, 5, 20, 86),
  (565, 'Corsair Vengeance RGB DDR4 3000MHz 32GB', 2749, 5, 20, 29),

  -- CPU
  (973, 'Intel Core i7-7700K Kaby Lake Processor', 2874, 3, 15, 12),
  (524, 'Intel Core i5-4460 Processor', 1499, 3, 15, 6),
  (433, 'AMD Ryzen 7 1700 Processor', 2929, 3, 15, 2),
  (612, 'AMD FX-8320 Black Edition Processor', 1099, 3, 15, 0),
  (901, 'Intel Core i3-7100 Kaby Lake Processor,', 899, 3, 15, 10),
  (053, 'Intel Pentium G4400 Skylake Processor', 479, 3, 15, 6),
  (767, 'AMD Ryzen 7 1800X Processor', 4155, 3, 15, 4),
  (483, 'AMD A4-7300 Processor', 319, 3, 15, 1),

  -- GPU
  (720, 'Sapphire Radeon RX 480 8GB NITRO+', 2249, 15, 40, 23),
  (497, 'ASUS GeForce GTX 1050 Ti 4GB ROG Strix', 1599, 15, 40, 2),

  -- Mainboard
  (749, 'ASUS Prime Z270-P, Socket-1151', 998, 4, 10, 8),
  (336, 'ASUS B85M-G, Socket-1150', 599, 4, 10, 2),
  (572, 'ASUS ROG Crosshair VI Hero, Socket-AM4', 2290, 4, 10, 6),
  (822, 'Gigabyte Aorus GA-AX370-Gaming 5, S-AM4', 1849, 4, 10, 1),
  (121, 'MSI 970A GAMING PRO CARBON, Socket AM3+', 969, 4, 10, 25),
  (806, 'MSI H110I Pro, Socket-1151', 579, 4, 30, 10),
  (016, 'ASUS Sabertooth 990FX R3.0, Socket-AM3+', 1590, 4, 30, 0),
  (807, 'MSI Z270 Tomahawk Arctic, Socket-1151', 1399, 4, 30, 17),
  (807, 'MSI A88XM-E35 V2, Socket FM2+', 495, 4, 30, 2),

  -- Cases
  (228, 'Corsair Carbide Clear 400C Midi Tower', 769, 7, 30, 22),
  (402, 'Fractal Define Nano S Black Window', 599, 7, 30, 8),
  (436, 'NZXT H440 New Edition Silent Ultra', 995, 7, 30, 2),
  (095, 'Fractal Design Node 202', 659, 7, 30, 7),
  (403, 'Fractal Design Node 304 Mini-ITX Hvid', 649, 7, 30, 23),
  (501, 'Be Quiet! Dark Base Pro 900 Oransje', 1995, 7, 30, 1),
  (059, 'Deepcool Dukase Liquid', 1899, 7, 30, 0),
  (631, 'In Win Mana 136 Midi Tower Hvid', 419, 7, 39, 12);

INSERT INTO RAMs (id, busSpeed, ramType) VALUES
  (640, 2133, 'DDR4'),
  (838, 1600, 'DDR3'),
  (368, 3200, 'DDR4'),
  (900, 2133, 'DDR4'),
  (605, 1333, 'DDR3'),
  (182, 2400, 'DDR4'),
  (254, 3200, 'DDR4'),
  (565, 3000, 'DDR4');

INSERT INTO CPUs (id, busSpeed, socket) VALUES
  (973, 2133, 'LGA1151'),
  (524, 1600, 'LGA1150'),
  (433, 3200, 'AM4'),
  (612, 1333, 'AM3'),
  (901, 2133, 'LGA1511'),
  (053, 2400, 'LGA1511'),
  (767, 3200, 'AM4'),
  (483, 1600, 'FM2');

INSERT INTO GPUs (id) VALUES
  (720),
  (497);

INSERT INTO ComputerCases (id, formFactor) VALUES
  (228, 'ATX'),
  (402, 'mATX'),
  (436, 'ATX'),
  (095, 'mITX'),
  (403, 'mITX'),
  (501, 'ATX'),
  (059, 'ATX'),
  (631, 'ATX');

INSERT INTO Mainboards (id, onBoardGPU, socket, formFactor, ramType) VALUES
  (749, TRUE, 'LGA1151', 'ATX', 'DDR4'),
  (336, TRUE, 'LGA1150', 'mATX', 'DDR3'),
  (572, FALSE, 'AM4', 'ATX', 'DDR4'),
  (822, FALSE, 'AM4', 'ATX', 'DDR4'),
  (121, FALSE, 'AM3', 'ATX', 'DDR3'),
  (806, TRUE, 'LGA1511', 'mITX', 'DDR4'),
  (016, FALSE, 'AM3', 'ATX', 'DDR3'),
  (807, TRUE, 'LGA1511', 'ATX', 'DDR4'),
  (807, TRUE, 'FM2', 'mATX', 'DDR3');

INSERT INTO ComputerSystems (computerSystemName, cpu, mainboard, gpu, ram, computerCase) VALUES
  ('Super Fast Build', 973, 749, NULL, 640, 228),
  ('Coolmaster masterComputer', 524, 336, NULL, 838, 402),
  ('AMD Ryzen Rig', 433, 572, 720, 368, 436),
  ('RGB everything', 767, 822, 497, 254, 059),
  ('EXTREME RGB SUPER HATRED SPITFIRE REVENGE GAMING MESSERSCHMIDT EDITION', 053, 806, 720, 182, 095),
  ('Sponsored by Mountain Dew, Doritos and Snoop Dogg', 483, 807, FALSE, 838, 402),
  ('No guarantee it wont burn your apartment down', 901, 807, 497, 900, 631),
  ('♪Deja Vu i''ve just been in this place before♪', 053, 806, 720, 182, 095);