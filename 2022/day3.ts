import { readFile } from "fs";

readFile(__dirname + "/inputs/input3.txt", (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const rucksacks = data.toString().split("\n").filter(sack => sack.length > 0);
    console.log(rucksacks.map(sack => {
        const half = Math.floor(sack.length / 2);
        const firstHalf = sack.slice(0, half);
        const secondHalf = sack.slice(half);
        const commonLetter = firstHalf.split("").filter(letter => secondHalf.includes(letter))[0];
        return commonLetter.charCodeAt(0) + (commonLetter === commonLetter.toLowerCase() ? -96 : -64 + 26);
    }).reduce((a, b) => a + b)); // part 1
    const groups: string[][] = [];
    while (rucksacks.length > 0) {
        groups.push(rucksacks.splice(0, 3));
    }
    console.log(groups.map(group => {
        const commonLetter = group[0].split("").filter(letter => group[1].includes(letter) && group[2].includes(letter))[0];
        return commonLetter.charCodeAt(0) + (commonLetter === commonLetter.toLowerCase() ? -96 : -64 + 26);
    }).reduce((a, b) => a + b)); // part 2
});
