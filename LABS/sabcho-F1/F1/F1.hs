--Sabrina Chowdhury
module F1 where

import Data.List
import System.IO 
import Data.Char

-- 1. fibonacci

fib :: Integer -> Integer -- tar in en integer och ger tillbaka en integer
fib 0 = 0
fib 1 = 1
fib n = fib (n-1) + fib (n-2)

-- 2. rovarsprak och karpsvaror


vowels :: String -- String typ
vowels = "aeiouy" -- vokaler

isVowel :: Char -> Bool -- char in och sant/falskt ut
isVowel x = x `elem` vowels -- om karaktären x är en vokal (alltså ingår i vowels) får vi sant

--pop blir p
karpsravor :: String -> String -- String in och String ut
karpsravor (x:y:z:xs) = -- x, y och z representerar de 3 första elementen/karaktärerna i en lista
 if not(isVowel x) && y == 'o' && x == z -- om x och z är samma konsonant och har 'o' mellan sig vill vi ersätta de tre bokstäverna med endast konsonanten x
 then x : karpsravor xs
 else x : karpsravor (y:z:xs) -- vi går vidare i listan, kollar på y,z och elementet efter z
karpsravor xs = xs

--p blir pop
rovarsprak :: String -> String -- String in och String ut
rovarsprak [] = [] -- basfall, när vi gått igenom alla element i listan
rovarsprak (x:xs) = -- x är första elementet i listan, xs är resten av listan
 if not(isVowel x) -- om x är en konsonant vill vi lägga till ett 'o' samt samma konsonant efter 'o':et
 then x : 'o' : x : rovarsprak xs
 else x : rovarsprak xs

 -- 3. medellangd

medellangd str =  fromIntegral (sum wordList)/ fromIntegral (length wordList)
-- fromIntegral för att inte få problem med typerna, Haskell är sträng
-- sum tar längden på alla ord sammanlagt
-- length tar längden på ordlistan, alltså antalet ord
 where wordList = map length (words (map (\x -> if not (isLetter x) then ' ' else x) str))
 -- map applicerar funktionen på alla element, alltså att vi kollar om det är en bokstav, och om inte så lägger vi istället till whitespace där
 -- words funktionen separerar strängen baserat på whitespace och lägger in alla ord som separata element
 -- map length tar längden på alla ord i ordlistan
 -- alltså är wordlist en lista som innehåller längden av ett antal ord som separata elementen

-- 4. skyffla

-- hjälpfunktion : everySecond tar in listan med element och separerar den så att var andra element hamnar i en ny lista : resultatet blir två listor, en med varje udda index och ett med varje jämnt index
everySecond [] = ([], []) -- basfall
everySecond [oneElement] = ([oneElement], []) --basfall
everySecond (firstElement:secondElement:restOfList) = (firstElement:oddIndexList, secondElement:evenIndexList) where (oddIndexList, evenIndexList) = everySecond restOfList


-- allLists tar in listan med udda index följt av den med jämna index och separerar den med jämna index vidare med hjälp av everySecond funktionen
-- skyffla skickar in listan i allLists
-- allLists skickar in listan i everySecond
-- everySecond tar listan och gör två listor (lista bestående av listor)
-- det första elementet går i den ena
-- det andra elementet går i den andra
-- sedan kollar vi på resten av listan
-- första och andra elementet går in i två olika listor, tills listan är tom
-- dessa två listor med varannat element skickar tillbaka till allLists som sparar den första listan/elementet
-- i listan skickad från everySecond som oddIndexes och sedan skickar in den andra listan i everySecond
-- den andra listan delas på två och skickas tillbaka till allLists
-- allLists fortsätter skicka sin senaste lista till everySecond ända tills vi är på vår sista lista
-- när hela listan sorterats av everySecond och sedan finns i ordning med hjälp av allLists som har den i form av flera listor
-- skickas listorna tillbaka till skyffla som konkatenerar dem
allLists :: [a] -> [[a]]
allLists [] = [] -- basfall
allLists [x] = [[x]] --basfall
allLists xs = oddIndexes : allLists evenIndexes where (oddIndexes,evenIndexes) = everySecond xs



-- skyffla konkatenerar de slutgiltiga listorna till en lista
skyffla :: [a] -> [a]
skyffla [] = [] -- basfall
skyffla xs =　concat $ allLists xs -- skickar in listan i alllists och konkatenerar resultatet
