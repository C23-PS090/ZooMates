-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 30, 2023 at 06:09 AM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `zoomates`
--

-- --------------------------------------------------------

--
-- Table structure for table `animal`
--

CREATE TABLE `animal` (
  `id_animal` int(5) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `animal`
--

INSERT INTO `animal` (`id_animal`, `name`, `description`) VALUES
(1, ' ant', ' Ants are small insects that live in organized colonies. They have a segmented body and six legs. Ants are known for their strong cooperation in finding food and protecting their nests.\n'),
(2, 'bear\r\n', 'Bears are large mammals that are usually found in forested areas. They have strong claws, large canines, and thick fur. Bears tend to be omnivorous, eating plants, fish and meat.\r\n'),
(3, 'bee\r\n', 'Bees are insects best known for producing honey. They have hairy bodies and two pairs of wings. Bees play an important role in pollinating plants and also form colonies with tasks divided within them.\r\n'),
(4, 'beetle\r\n', 'Beetles are insects with hard bodies that are usually equipped with wings. There are many types of beetles with different shapes and colors. They have an important role in the ecosystem as organic waste eaters and pollinators.\r\n'),
(5, 'buffalo\r\n', 'Buffaloes are large-bodied animals with long curved horns. They are often used as draft animals in some countries. Buffaloes are known for their physical strength and endurance.\r\n'),
(6, 'butterfly\r\n', ' Butterflies are insects that have brightly colored wings and a distinctive body shape. Their initial stage of life is as a caterpillar or chrysalis before turning into a butterfly. Butterflies are important pollinators in the ecosystem.\r\n'),
(7, 'camel\r\n', 'Camels are four-legged animals that live in desert areas. They have long necks and can withstand extreme weather. Camels are often used as transport animals and provide a source of food and materials for humans.\r\n'),
(8, 'cat\r\n', 'Cats are popular pets. They have small to medium-sized bodies, sharp claws, and smooth fur. Cats are independent animals, but can also be playful companions.\r\n'),
(9, 'caterpillar\r\n', 'Caterpillars are the larvae of the tiger butterfly that have dense and attractive feathers. They are usually brightly colored as a warning to potential predators. Caterpillars can cause irritation if touched.\r\n'),
(10, 'centipede\r\n', 'Millipedes are insects that have long bodies and many legs. They are often found under rocks or in damp areas. Millipedes feed on decaying organic matter and are not considered harmful to humans.\r\n'),
(11, 'chameleon\r\n', 'Chameleons are reptiles that have the ability to change their skin color according to their surroundings. They have long, sticky tongues to catch prey. Chameleons also have the ability to move slowly to camouflage themselves.\r\n'),
(12, 'cheetah\r\n', 'Cheetahs are extremely fast predatory animals and are known to be the fastest running animals on land. They have a slender body, patchy fur, and a long tail to maintain balance while running at high speeds.\r\n'),
(13, 'chicken\r\n', 'Chickens are poultry animals that are often used as a source of meat and eggs. They have small wings and cannot fly far. Chickens usually live in groups and have various breeds with different physical characteristics.\r\n'),
(14, 'cockroach\r\n', ' Cockroaches are insects that are often found in humid areas such as homes and dirty places. They have a flattened body, wings that cover the body when not in use, and the ability to survive in harsh conditions.\r\n'),
(15, 'cow\r\n', 'Cows are important farm animals in the livestock industry. They have large bodies, four legs, and horns in some types. Cattle are herbivorous animals known for providing meat, milk, and other materials.\r\n'),
(16, 'crab\r\n', 'Crabs are marine animals that have a body protected by a hard shell. They have a pair of strong claws that are used to protect themselves and search for food on the sea floor. Crabs have excellent flavor and are a popular dish in many cultures.\r\n'),
(17, 'crocodile\r\n', 'Crocodiles are large reptiles that live in freshwater. They have a body covered in scaly skin, a wide mouth with sharp teeth, and a long tail. Crocodiles are powerful predators and tend to hunt prey near water.\r\n'),
(18, 'deer\r\n', 'Deer are common mammals found in various habitats around the world. They have slender bodies, long legs, and antlers in male deer. Deer are herbivores and often live in herds.\r\n'),
(19, 'dog\r\n', 'Dogs are very popular pets and are close to humans. They come in a variety of breeds with different sizes, shapes and personalities. Dogs are social, intelligent, and are often used in a variety of jobs such as guards, helpers, and tracking animals.\r\n'),
(20, 'dolphin', 'Dolphins are marine mammals that live in warm waters. They have aerodynamic bodies, strong fins, and the ability to swim quickly. Dolphins are very intelligent and often interact with humans.\r\n'),
(21, 'dragonfly\r\n', 'Dragonflies are insects with transparent wings and long, slender bodies. They have agile flying skills and are often found around freshwater. Dragonflies are important pollinators in the ecosystem.\r\n'),
(22, 'duck\r\n', 'Ducks are waterfowl that have broad beaks and short legs. They are often found in lakes, rivers, and other watery areas. Ducks are migratory animals and have various species with different feather colors and patterns.\r\n'),
(23, 'eagle\r\n', 'Eagles are birds of prey that have large size, broad wings, and sharp eyesight. They are known for their high flying ability and ability to hunt prey in the air. Eagles are a symbol of strength and freedom.\r\n'),
(24, 'echidna\r\n', 'Hedgehogs are mammals that have bodies covered in sharp spines. They have a long snout and short legs. Hedgehogs use their spines as a defense against predators. They are herbivores and are often found in forested areas.\r\n'),
(25, 'eel\r\n', 'Eels are fish that have long, slippery, non-muscular bodies. They are often found in fresh and brackish waters. Eels have the ability to burrow in mud and wet soil.\r\n'),
(26, 'elephant\r\n', 'Elephants are the largest mammals on land. They have large bodies, long trunks, and large tusks in males. Elephants are herbivores and live in complex social groups. They are highly intelligent and have a strong memory.\r\n'),
(27, 'flamingo\r\n', 'Flamingos are birds with long necks and bright plumage. They are often found in swamps and shallow lakes. Flamingos are unique birds with the ability to stand on one leg and use their curved beak to filter food from the water.\r\n'),
(28, 'fly\r\n', 'Flies are small insects that fly with transparent wings. They are often found around decaying food sources. Flies have a short life cycle and can be a pest in homes and farms\r\n'),
(29, 'fox\r\n', 'Foxes are mammals known for their thick fur and long tails. They are well adapted and are often found in various habitats around the world. Foxes are clever predators and often hunt at night.\r\n'),
(30, 'frog\r\n', 'Frogs are amphibians that live in both water and land. They have smooth skin, claws on the toes, and good jumping ability. Frogs undergo metamorphosis from the small frog (toad) stage to an adult frog.\r\n'),
(31, 'giraffe\r\n', ' Giraffes are mammals that have very long necks and tall legs. They are known for their height of several meters. Giraffes have long tongues and usually live in grassland or savanna areas.\r\n'),
(32, 'goldfish\r\n', 'Goldfish are freshwater fish that are popular as ornamental fish. They have slender bodies with bright colors. Goldfish are often kept in aquariums and ponds.\r\n'),
(33, 'goose\r\n', 'Swans are water birds that have long necks and strong wings. They are often found in lakes and rivers. Swans are known for their long migrations and form organized social groups.\r\n'),
(34, 'gorilla\r\n', 'Gorillas are the largest primate and are very similar to humans. They have large bodies, strong arms, and live in the African rainforest. Gorillas are herbivorous animals that live in groups led by dominant males.\r\n'),
(35, 'grasshopper\r\n', 'Grasshoppers are insects that have the ability to jump long distances using their long hind legs. They are often found in grassy areas and can produce a unique sound by rubbing their wings.\r\n'),
(36, 'hamster\r\n', ' Hamsters are rodents that are popular as pets. They have small bodies, short ears, and enlarged cheeks to store food. Hamsters are nocturnal and tend to be active at night.\r\n'),
(37, 'harborseal\r\n', 'Harbor seals are marine mammals that live in the cold waters of the northern hemisphere. They have slender bodies, strong front flippers, and the ability to swim quickly. Harbor seals are often found around beaches and ice.\r\n'),
(38, 'hedgehog\r\n', 'Hedgehogs are small mammals that have bodies covered in sharp spines. They roll themselves into a ball when they feel threatened. Hedgehogs are nocturnal and live in a variety of habitats such as forests and grasslands.\r\n'),
(39, 'hippopotamus\r\n', 'Hippos are the largest aquatic mammals in the world. They have a large body, a wide mouth, and a raised nose. Hippos live in lakes and rivers in Africa and are known for spending most of their time in the water\r\n'),
(40, 'horse\r\n', 'Horses are large mammals that have been domesticated by humans for thousands of years. They have strong bodies, long legs, and bushy tails. Horses are used for various human activities such as transportation, agriculture, and sports.\r\n'),
(41, 'hyena\r\n', 'Hyenas are mammals known for their distinctive laughing sound. They have slender bodies, large heads, and strong teeth. Hyenas are predatory animals that live in groups and usually hunt prey at night.\r\n'),
(42, 'iguana\r\n', 'Iguanas are reptiles that have large bodies, long tails, and rough scales. They are often found in tropical and desert areas. Iguanas are herbivores that live in trees and have the ability to sunbathe to regulate their body temperature.\r\n'),
(43, 'jaguar\r\n', 'Jaguars are large cats found in South America and Central America. They have a slender body, spotted fur, and incredible strength. Jaguars are formidable predators and skilled at hunting.\r\n'),
(44, 'jellyfish\r\n', 'Jellyfish are marine animals consisting of a transparent body and long tentacles. They have no brain or spine and use their tentacles to catch prey. Some jellyfish can sting and are toxic to humans.\r\n'),
(45, 'kangaroo\r\n', 'Kangaroos are mammals known for their ability to jump long distances using their powerful hind legs. They are often found in Australia. Kangaroos have a pouch on their stomach where they carry and care for their young.\r\n'),
(46, 'killerwhale\r\n', 'Orcas, also known as killer whales, are marine mammals that are top predators in the sea. They have large bodies, distinctive dorsal fins, and sharp teeth. Orcas live in organized social groups and have complex communication skills.\r\n'),
(47, 'koala\r\n', 'Koalas are mammals famous for their habit of eating eucalyptus leaves. They have small bodies, long ears, and strong claws for climbing trees. Koalas are endemic to Australia and are often considered a symbol of the country.\r\n'),
(48, 'komodo\r\n', 'Komodo dragons are the world\'s largest lizards found on Komodo Island, Indonesia. They have large bodies, rough skin, and strong teeth. Komodo dragons are skilled predators and use the venom in their saliva to attack prey.\r\n'),
(49, 'leopard\r\n', 'Leopards are large cats found in various habitats throughout Africa and Asia. They have a sleek body, spotted fur, and exceptional speed and hunting skills.\r\n'),
(50, 'lion\r\n', 'Lions are big cats known as the \"king of the jungle\". They have large bodies, striking beards on males, and great strength. Lions live in social groups called \"packs\" and usually share roles between males and females.\r\n'),
(51, 'lionfish\r\n', 'Lionfish are saltwater fish that have sharp spines on their bodies. They have striking patterns and colors, and their spines are poisonous. Lionfish are commonly found in tropical waters and coral reefs.\r\n'),
(52, 'lizard\r\n', 'Lizards are reptiles that are widely distributed around the world. They have long bodies, tapered tails, and the ability to launch their tails to escape. Lizards have various species and varying physical features.\r\n'),
(53, 'llama\r\n', 'Llamas are mammals native to South America that belong to the camel and camelid family. They have large bodies, thick fur, and long necks. Llamas are often used as livestock and transportation in the mountains.\r\n'),
(54, 'monkey\r\n', 'Monkeys are primates found in various parts of the world. They have agile bodies, long tails, and the ability to use tools. Monkeys live in social groups and have complex social skills.\r\n'),
(55, 'mosquito\r\n', 'Mosquitoes are insects known for sucking the blood of humans and other animals. They have small bodies, transparent wings, and long proboscis to suck blood. Mosquitoes are vectors of diseases like malaria, dengue, and yellow fever.\r\n'),
(56, 'moth\r\n', 'Moths are insects with broad wings and hairy bodies. They are generally active at night and withdraw during the day. Moths often have beautiful colors and patterns on their wings.\r\n'),
(57, 'mouse\r\n', 'Mouse are rodents found all over the world. They have small bodies, long ears and tails, and teeth that grow continuously. Mice are nocturnal animals and are often pests in homes or farms.\r\n'),
(58, 'orangutan\r\n', 'Orangutans are great apes found in the rainforests of Southeast Asia. They have large bodies, long arms, and prominent heads. Orangutans are intelligent social animals and spend most of their time in trees.\r\n'),
(59, 'ostrich\r\n', 'Ostriches are large birds found in the grasslands and deserts of Africa and the Middle East. They have tall bodies, long necks, and large wings. Ostriches cannot fly but have a high running speed.\r\n'),
(60, 'otter\r\n', 'Otters are aquatic mammals found in fresh waters in various parts of the world. They have long bodies, dense fur, and strong claws for climbing and swimming. Otters are highly skilled at foraging for food in water.\r\n'),
(61, 'owl\r\n', 'Owls are predatory birds known for their large eyes and silent flying ability. They are often found in forests and dark environments. Owls have keen hearing abilities and are often active at night.\r\n'),
(62, 'panda\r\n', 'Pandas are mammals known for their black and white fur. They are herbivorous animals that live in the mountains of China. Pandas are protected animals and are often considered a symbol of species preservation.\r\n'),
(63, 'parrot\r\n', 'Parrots are birds with the ability to mimic human voices and other sounds. They have small bodies, strong beaks, and bright plumage. Parrots are social birds and have exceptional learning abilities.\r\n'),
(64, 'peacock\r\n', 'Peacocks are birds with long tails and colorful feathers. They are often found in forests and rainforests in Asia. Male peacocks are known for their spectacular displays when showing off their tails.\r\n'),
(65, 'penguin\r\n', 'Penguins are flightless seabirds found in the southern hemisphere. They have slender bodies, wings that adapt into flippers, and good swimming skills. Penguins live in large groups and usually live in icy environments.\r\n'),
(66, 'pig\r\n', 'Pigs are mammals found all over the world. They have large bodies, long noses, and short legs. Pigs come in many varieties, such as wild boars and farm pigs, and are often used as a food source.\r\n'),
(67, 'platypus\r\n', 'Platypuses are small mammals found in Australia. They have a duck-like bill, webbed feet, and the ability to lay eggs. Platypuses are unique as they are the only mammals that lay eggs and have poisonous glands on their hind legs.\r\n'),
(68, 'polarbear\r\n', 'Polar Bears are large mammals found in the Arctic regions. They have a thick layer of fat, white fur, and good swimming ability. Polar bears depend on sea ice for food and are often linked to global climate change.\r\n'),
(69, 'rabbit\r\n', 'Rabbits are small mammals with long ears and constantly growing teeth. They are found in a variety of habitats around the world. Rabbits are herbivorous animals that are often kept as pets or used as a food source.\r\n'),
(70, 'raccoon\r\n', 'raccoon	Raccoons are mammals found in North America and Central America. They have a small to medium-sized body, a fluffy tail, and a characteristic black \"mask\" around the eyes. Raccoons are agile animals and often forage around humans.\r\n'),
(71, 'rhinoceros\r\n', 'Rhinos are large mammals known for having thick skin and horns on their noses. They are found in Africa and Asia. Rhinos are endangered due to illegal hunting and habitat loss.\r\n'),
(72, 'salamander\r\n', 'Salamanders are amphibians found in different parts of the world. They have long bodies, tapered tails, and the ability to regenerate lost limbs. Salamanders generally live in fresh waters and under rotting wood.\r\n'),
(73, 'scorpion\r\n', 'Scorpions are arachnids that have a segmented body, powerful claws, and a tail that contains venom. They are found in various habitats around the world. Some species of scorpions can produce stings that are harmful to humans.\r\n'),
(74, 'searays\r\n', 'Sea Rays are marine fish with a flat body and pectoral fins that turn into wings. They are found in tropical waters and often have interesting patterns and colors. Sea Rays have the ability to swim by swinging their wings.\r\n'),
(75, 'seaturtle\r\n', 'Sea Turtles are marine reptiles found in warm waters around the world. They have large bodies, hard shells, and the ability to breathe in water and on land. Sea Turtles are protected animals due to the threat of poaching and habitat destruction.\r\n'),
(76, 'seaurchins\r\n', 'Sea Hedgehogs are marine animals with spiny bodies that live on coral reefs. They have spines that are poisonous and serve as protection from predators. Sea Hedgehogs come in many different species with different shapes and sizes.\r\n'),
(77, 'shark\r\n', ' Sharks are predatory fish found in oceans around the world. They have slender bodies, large fins, and sharp teeth. Sharks are scary animals but are important in maintaining the balance of the marine ecosystem.\r\n'),
(78, 'sheep\r\n', 'Sheep are farm animals known for their dense wool and are used to obtain wool. They have fat bodies, coiled horns, and are herbivorous animals. Sheep are often raised for their meat, milk, and wool.\r\n'),
(79, 'slug\r\n', ' Snails are molluscs that have a soft body protected by a shell. They move slowly using their abdominal legs and are often found in a variety of habitats, including land, freshwater, and sea.\r\n'),
(80, 'snail\r\n', 'Conchs are molluscs that have a soft body protected by a distinctive shell. They have abdominal legs and are characterized by the ability to retract their body into the shell when threatened. Conchs live in a variety of habitats, including land and freshwater.\r\n'),
(81, 'snake\r\n', 'Snakes are reptiles known for their long bodies and lack of legs. They are predators that use poisonous or constricting teeth to capture and prey on their prey. Snakes have various species with different characteristics and behaviors.\r\n'),
(82, 'spider\r\n', 'Spiders are insects known for having eight legs and intricate webs. They are predators that catch prey by using their webs. Spiders have various species with diverse sizes and colors.\r\n'),
(83, 'squirrel\r\n', 'Squirrels are small animals with slender bodies and long tails. They have strong claws for climbing and sharp teeth for chewing food. Squirrels are often found in forests and parks as wild or domesticated animals.\r\n'),
(84, 'starfish\r\n', 'Starfish are marine animals that have bodies with branching arms. They are found on the ocean floor in a variety of habitats, from shallow water to deep water. Starfish can have a variety of beautiful colors and patterns.\r\n'),
(85, 'swan\r\n', 'Swans are water birds known for their long necks and wide beaks. They are often found in lakes, rivers and other wetlands. Swans are often migratory and live in large groups.\r\n'),
(86, 'tiger\r\n', 'Tigers are large cats known as powerful and dangerous predators. They have large bodies, mottled fur and sharp teeth. Tigers live in the forests and savannas of Asia and are one of the most endangered big cats.\r\n'),
(87, 'turtle\r\n', 'Tortoises are reptiles with hard shells that protect their bodies. They have flat bodies and short tails. Turtles live in a variety of habitats, including land and freshwater, and often have long lives.\r\n'),
(88, 'whale\r\n', 'Whales are the largest marine mammals in existence. They have large bodies, dorsal fins and powerful tails. Whales are ocean-dwelling animals and breathe using their nostrils called \"blowholes\". They are known for their long migrations and singing voices.\r\n'),
(89, 'wolf\r\n', 'Wolves are wild animals related to dogs. They have slender bodies, strong claws, and sharp teeth. Wolves are social animals that live in groups called \"wolf packs\". They are often found in forest and grassland habitats in different parts of the world.\r\n'),
(90, 'zebra\r\n', 'Zebras are animals known for their black and white striped pattern on their body. They are herbivorous mammals often found on grasslands in Africa. Zebras live in large groups and have the ability to run fast.\r\n');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `animal`
--
ALTER TABLE `animal`
  ADD PRIMARY KEY (`id_animal`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `animal`
--
ALTER TABLE `animal`
  MODIFY `id_animal` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
