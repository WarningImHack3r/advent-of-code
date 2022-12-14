import { readFile } from "fs";

const day = __filename.split("/").pop()?.match("\\d+")?.[0];
readFile(__dirname + `/inputs/input${day}.txt`, (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const content = data.toString();
    const all = content.split("\n\n");
    const finalArray = all.map(group => group.split("\n").map(number => parseInt(number)).reduce((a, b) => a + b)).filter(number => !isNaN(number));
    console.log(Math.max(...finalArray)); // part 1
    console.log(finalArray.sort().reverse().slice(0, 3).reduce((a, b) => a + b)); // part 2
});
