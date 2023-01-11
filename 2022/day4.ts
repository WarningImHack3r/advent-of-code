import { readFile } from "fs";

const day = __filename.split("/").pop()?.match("\\d+")?.[0];
readFile(__dirname + `/inputs/input${day}.txt`, (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const input = data.toString().split("\n").filter(x => x.length > 0);
    const arrays = input.map(line => {
        const [range1, range2] = line.split(",");
        let [digit1, digit2] = range1.split("-").map(x => parseInt(x));
        const arr = Array.from({ length: digit2 - digit1 + 1 }, (_, i) => i + digit1);
        [digit1, digit2] = range2.split("-").map(x => parseInt(x));
        const arr2 = Array.from({ length: digit2 - digit1 + 1 }, (_, i) => i + digit1);
        return [arr, arr2];
    });
    console.log(arrays.map(array => {
        const overlap = array[0].filter(x => array[1].includes(x)).length;
        return (overlap === array[0].length || overlap === array[1].length ? 1 : 0) as number;
    }).reduce((a, b) => a + b)); // part 1
    console.log(arrays.map(array => Math.min(1, array[0].filter(x => array[1].includes(x)).length)).reduce((a, b) => a + b)); // part 2
});
