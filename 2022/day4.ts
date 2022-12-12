import { readFile } from 'fs';

readFile("2022/inputs/input4.txt", (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const input = data.toString().split("\n").filter(x => x.length > 0);
    const first: number[] = input.map(line => {
        const [range1, range2] = line.split(",");
        let digit1 = parseInt(range1.split("-")[0]);
        let digit2 = parseInt(range1.split("-")[1]);
        const arr = Array.from({ length: digit2 - digit1 + 1 }, (_, i) => i + digit1);
        digit1 = parseInt(range2.split("-")[0]);
        digit2 = parseInt(range2.split("-")[1]);
        const arr2 = Array.from({ length: digit2 - digit1 + 1 }, (_, i) => i + digit1);
        const overlap = arr.filter(x => arr2.includes(x)).length;
        return overlap === arr.length || overlap === arr2.length ? 1 : 0;
    })
    console.log(first.reduce((a, b) => a + b)); // part 1
    console.log(input.map(line => {
        const [range1, range2] = line.split(",");
        let digit1 = parseInt(range1.split("-")[0]);
        let digit2 = parseInt(range1.split("-")[1]);
        const arr = Array.from({ length: digit2 - digit1 + 1 }, (_, i) => i + digit1);
        digit1 = parseInt(range2.split("-")[0]);
        digit2 = parseInt(range2.split("-")[1]);
        const arr2 = Array.from({ length: digit2 - digit1 + 1 }, (_, i) => i + digit1);
        return Math.min(1, arr.filter(x => arr2.includes(x)).length);
    }).reduce((a, b) => a + b)); // part 2
});
