--Sabrina Chowdhury
module F2 where
import Data.List

data MolSeq = MolSeq { molSeqName :: String
                     , molSeq :: String
                     , molSeqType :: MolType
                     } deriving (Eq, Show, Read)

data MolType = DNA | Protein deriving (Show, Eq, Read)


isDNA :: String -> Bool
isDNA (x:xs)
  | xs == [] && isDNACharacter x == True = True
  | xs /= [] && isDNACharacter x == True = isDNA xs
  | otherwise = False

isDNACharacter :: Char -> Bool
isDNACharacter character = elem character "ACGT"

string2seq :: String -> String -> MolSeq
string2seq name molseq
   | isDNA molseq == True = MolSeq name molseq DNA
   | otherwise = MolSeq name molseq Protein



seqName :: MolSeq -> String
seqName (MolSeq molName _ _) = molName


seqSequence :: MolSeq -> String
seqSequence (MolSeq _ molSeq _ ) = molSeq


seqLength :: MolSeq -> Int
seqLength (MolSeq _ molSeq _) = length molSeq


--Funktion som returnerar typ
seqTyp :: MolSeq -> MolType
seqTyp (MolSeq _ _ seqType ) = seqType



seqDistance :: MolSeq -> MolSeq -> Double
seqDistance molA molB
   | seqTyp molA /= seqTyp molB  = error "Can´t compare Protein with DNA"
   | seqTyp molA == DNA  && alfa <= 0.74   = -3/4 * log(1 - 4/3 * alfa)
   | seqTyp molA == DNA  && alfa > 0.74   = 3.3
   | seqTyp molA == Protein && alfa <= 0.94   = -19/20 * log(1 - 20 * alfa / 19)
   | seqTyp molA == Protein && alfa > 0.94   = 3.7
   where
        alfa = fromIntegral lengthDiff / fromIntegral length
        lengthDiff = hamming (seqSequence molA) (seqSequence molB)
        length = seqLength molA


hamming :: String -> String -> Int
hamming la lb = length (filter id (zipWith (/=) la lb))


data Profile = Profile {matrix :: [[(Char, Int)]]
                     ,molType :: MolType
                     ,nrOfSeq :: Int
                     ,profName :: String
                     } deriving (Show)



molseqs2profile :: String -> [MolSeq] -> Profile
molseqs2profile stringName molSeqList = Profile matrix molType nrOfSeq profName
        where
            -- skickar in lista med molseqs i makeProfileMatrix för att få ut deras corresponding matrisprofiler
            matrix = makeProfileMatrix molSeqList
            -- skicka in den första molseqen i molseq-listan för att veta om det är DNA eller Protein
            molType = seqTyp (head molSeqList)
            -- kolla hur många molseqs vi har i molseq-listan
            nrOfSeq = length molSeqList
            -- vi ger profilen ett namn (stringname) som sparas i profName
            profName = stringName

-- vad DNA kan innehålla
nucleotides = "ACGT"
-- vad protein kan innehålla
aminoacids = sort "ARNDCEQGHILKMFPSTWYVX"
--skicka in en lista med molseqs och få ut deras matrisprofiler, där varje position definieras av char i y-led och int i x-led
-- ex för DNA har vi char ACGT och Int 1,2,3..n där n är antalet sekvenser
makeProfileMatrix :: [MolSeq] -> [[(Char,  Int)]]
-- om vi skickar in en molseq-lista som är tom får vi error
makeProfileMatrix [] = error "Empty sequence list"
-- molseq-listan sl definieras som res
-- res är
makeProfileMatrix sl = res
 where
   -- kollar den första molseqen i molseq-listan sl med hjälp av funktionen seqTyp för att se om molseqen är DNA eller Protein
   t = seqTyp (head sl)
   -- defaults är en funktion som vi går in i när vi skriver defaults i koden
   -- om första molseqen i molseq-listan är DNA så skapar vi
   defaults = if t == DNA then
                  -- zip fungerar såhär zip :: [a] -> [b] -> [(a,b)]
                  -- zip [1,2,3] [2,3] returns [(1,2),(2,3)]
                  -- tar två listor och zippar ihop dem
                  zip nucleotides (replicate (length nucleotides) 0)
              -- om den första molseqen i molseq-listan är Protein
              else
                  -- då zippar vi istället listan med aminoacidskaraktärerna med en lika lång listor fylld med nollor
                  zip aminoacids (replicate (length aminoacids) 0)

   -- skickar in molseq-listan i seqSequence för att replacea alla molseqs i listan med endast deras sekvenser
   -- strs är alltså nu en lista som endast består av en massa olika sekvenser
   strs = map seqSequence sl

   -- i tmp1 behandlar vi strs - alltså listan med endast sekvenserna
   -- head x tar den första karaktären i alla sekvenser
   -- length tar längden av alla sekvenser
   -- transpose [[1,2,3],[4,5,6]] == [[1,4],[2,5],[3,6]]
   -- transpose strs skapar en lista där det första elementet innehåller det första elementet i alla sekvenser i strs
   -- det andra elementet innehåller sedan då det andra elementet i alla sekvenser i strs
   -- det som ska mappas på denna nya transponerade lista är då :
   -- att vi grupperar alla element ex [ACACA] blir [CCAAA]
   -- sedan sorterar vi dessa [CCAAA] blir [AAACC]
   -- ok så det som händer
   -- kör detta i terminalen

   -- test1
   --{molSeqName = "T1", molSeq = "ACATAA", molSeqType = DNA},MolSeq {molSeqName = "T2", molSeq = "AAGTCA", molSeqType = DNA},MolSeq {molSeqName = "T3", molSeq = "ACGTGC", molSeqType = DNA},MolSeq {molSeqName = "T4", molSeq = "AAGTTC", molSeqType = DNA},MolSeq {molSeqName = "T5", molSeq = "ACGTAA", molSeqType = DNA}]

   -- let strs = map seqSequence test1
   -- strs
   -- ["ACATAA","AAGTCA","ACGTGC","AAGTTC","ACGTAA"]

   -- import Data.list
   -- transpose strs
   -- ["AAAAA","CACAC","AGGGG","TTTTT","ACGTA","AACCA"]

   -- map (group . sort)(transpose strs)
   -- [["AAAAA"],["AA","CCC"],["A","GGGG"],["TTTTT"],["AA","C","G","T"],["AAA","CC"]]

   -- map (map (\x -> ((head x), (length x))) . group . sort)(transpose strs)
   -- [[('A',5)],[('A',2),('C',3)],[('A',1),('G',4)],[('T',5)],[('A',2),('C',1),('G',1),('T',1)],[('A',3),('C',2)]]

   -- Vår slutgiltiga lista tmp1 innehåller alltså information om antalet bokstäver i varje sekvens och visar dem i bokstavsordning i sekvensordning (alltså i separata listor)

   tmp1 = map (map (\x -> ((head x), (length x))) . group . sort)(transpose strs)

   -- equalFst returnernar true om a och b är likadana, annars false
   equalFst a b = (fst a) == (fst b)
   --
   res = map sort (map (\l -> unionBy equalFst l defaults) tmp1)


profileName :: Profile -> String
profileName (Profile _ _ _ name) = name


getprofileMatrix :: Profile -> [[(Char, Int)]]
getprofileMatrix (Profile profileMatrix _ _ _) = profileMatrix


profileFrequency :: Profile -> Int -> Char -> Double
profileFrequency (Profile matrix _ nrOfSeq _ ) pos char = fromIntegral charCount / fromIntegral nrOfSeq
       where  charCount = findCharCount ( matrix !! pos) char

findCharCount :: [(Char,  Int)] -> Char -> Int
findCharCount (x:xs) char
 | fst x == char = snd x-- get the Int value if the char is right
 | otherwise = findCharCount xs char -- gå vidare i listan (AKA en rad i matrisen) om karaktären inte stämmer


profileDistance :: Profile -> Profile -> Double
profileDistance p1 p2 = sumMatrices m1 m2 p1 p2
          where
                 m1 = matrix p1
                 m2 = matrix p2

                 -- tar ut matriserna för att lägga in i sumMatrices, tillsammans med deras profiler

sumMatrices :: [[(Char, Int)]] -> [[(Char, Int)]] -> Profile -> Profile -> Double
sumMatrices [] [] _ _ = 0
-- om matriserna som skickas in är tomma så är distansen 0 såklart
sumMatrices m1 m2 p1 p2 = ab + sumMatrices (tail m1) (tail m2) p1 p2
          where ab = absCalc (head m1) (head m2) p1 p2
          -- tar ut första profilen



absCalc :: [(Char, Int)] -> [(Char, Int)] -> Profile -> Profile -> Double
absCalc [] [] _ _ = 0
absCalc m1 m2 p1 p2 = abs(calcM1 - calcM2) + absCalc (tail m1) (tail m2) p1 p2
          where
               calcM1 = fromIntegral (snd (head m1)) / fromIntegral (nrOfSeq p1)
               calcM2 = fromIntegral (snd (head m2)) / fromIntegral (nrOfSeq p2)



class  Evol a  where
  distance :: a -> a -> Double
  name :: a -> String
  distanceMatrix :: [a] -> [(String,String, Double)]
  distanceMatrix a = dMAddIterator a

{-
  gör en ny lista som är lika lång som denna lista - 1
  varje element är en tupel, fyll alla tupler med det första elementet
  zippa listan med listan minus det första elementet
  gå vidare, listan utan första elementet
  gör samma sak
  gör en lista lika lång som denna nya lista -1
  fyllt alla tupler med det första elementet
  zippa listan med listan utan det första elementet
  gör om dessa steg tills det inte finns några element kvar
  för alla steg, istället för x och y, kör name , name  och sedan dist
  length [1,2,3,4,5] = n
  name x, name xs !! n-5, dist x xs !! 0
  name x, name xs !! n-4, dist x xs !! 1
  name x, name xs !! n-3, dist x xs !! 2
  name x, name xs !! n-2, dist x xs !! 3
  name x, name xs !! n-1, dist x xs !! 4
  | n == 0 = distanceMatrix xs
  where
    index = n-1
  name x, name xs !! 1, dist x xs !! 1
  name x, name xs !! 2, dist x xs !! 2
  name x, name xs !! 3, dist x xs !! 3
  name x, name xs !! 4, dist x xs !! 4
  distanceMatrix xs
  name x, name xs !! 1, dist x xs !! 1
  name x, name xs !! 2, dist x xs !! 2
  name x, name xs !! 3, dist x xs !! 3
  distanceMatrix xs
  name x, name xs !! 1, dist x xs !! 1
  name x, name xs !! 2, dist x xs !! 2
  distanceMatrix xs
  name x, name xs !! 1, dist x xs !! 1
  x y
  x z
  x å
  x ä
  x ö
  y z
  y å
where
    dist = distance x y
    n1 = name x
    n2 = name y
    -}



instance Evol MolSeq where
  distance molseq1 molseq2  = seqDistance molseq1 molseq2
  name molseq = seqName molseq
  


instance Evol Profile where
  distance profile1 profile2 = profileDistance profile1 profile2
  name profile = profileName profile
  

dMAddIterator a = dMIterator a []

dMIterator a iteratorList
  | length a == 0 = iteratorList
  | otherwise = dMIterator (tail a) ((iteratorList) ++ [(name (head a), name x, (distance (head a) x)) | x <- a])
