import { readFile } from "fs";

readFile("2022/inputs/input5.txt", (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const input = data.toString().split("\n");

    // Parse input
    const setup = input.slice(0, input.indexOf("")).map(line => line.replace(/\[|\]/g, ""));
    setup.pop();
    const layers = setup.map(line => line.split(" ")).map(layer => {
        let lastLetterIndex = 0;
        return layer.filter((letter, index, arr) => { // remove irrelevant blank letters
            if (letter !== "") {
                lastLetterIndex = index;
                return true;
            }
            let keep = !(letter === "" && index > 0 && arr[index - 1] === "");
            // check if current index is a multiple of 4 after lastLetterIndex
            if (index > lastLetterIndex) {
                keep = (index - lastLetterIndex) % 4 === 0;
            }
            return keep;
        });
    });
    // Format setup into arrays
    const arrays: string[][] = [];
    for (let i = 0; i < layers[0].length; i++) {
        arrays.push([]);
        for (let j = 0; j < layers.length; j++) {
            arrays[i].push(layers[j][i]);
        }
        arrays[i] = arrays[i].filter(letter => letter !== "");
    }
    // Loop through instructions and parse them
    const instructions = input.slice(input.indexOf("") + 1).filter(line => line !== "");
    // Execute instructions with a function that processes them
    const parsedInstructions = instructions.map(line => line.match(/\d+/g)?.map(Number) ?? []).map(instruction => {
        return [instruction[0], instruction[1] - 1, instruction[2] - 1];
    });
    const arrays2 = arrays.map(arr => arr.slice());
    parsedInstructions.forEach(instruction => {
        const [count, original, target] = instruction;
        executeMove(count, arrays[original], arrays[target]);
    });
    parsedInstructions.forEach(instruction => {
        const [count, original, target] = instruction;
        executeMove(count, arrays2[original], arrays2[target], "all-at-once");
    });
    // Return indexes 0 of each array in an array
    console.log(arrays.map(arr => arr[0]).join("")); // part 1
    console.log(arrays2.map(arr => arr[0]).join("")); // part 2
});

function executeMove(count: number, original: string[], target: string[], method: "one-by-one" | "all-at-once" = "one-by-one") {
    if (method === "one-by-one") {
        for (let i = 0; i < count; i++) {
            target.unshift(original.shift() ?? "");
        }
    } else {
        target.unshift(...original.splice(0, count));
    }
}
