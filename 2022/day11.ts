import { readFile } from "fs";
import { cloneDeep } from "lodash";
import { lcm } from "mathjs";

const day = __filename.split("/").pop()?.match("\\d+")?.[0];
readFile(__dirname + `/inputs/input${day}.txt`, (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const input = data.toString().split("\n").filter(x => x.length > 0);

    const monkeys: Monkey[] = [];
    // Parse input into 8 monkeys objects
    let currentMonkey = new Monkey();
    input.forEach(line => {
        line = line.trim();
        if (line.startsWith("Monkey")) {
            currentMonkey = new Monkey();
            currentMonkey.id = parseInt(line.match(/\d+/)?.[0] ?? "");
        } else if (line.startsWith("Starting items")) {
            currentMonkey.items = line.match(/\d+/g)?.map(x => parseInt(x)) ?? [];
        } else if (line.startsWith("Operation")) {
            const splitted = line.split(" ");
            currentMonkey.operationString = splitted.slice(splitted.length - 2);
        } else if (line.startsWith("Test")) {
            currentMonkey.testDivisibleValue = parseInt(line.match(/\d+/)?.[0] ?? "");
        } else if (line.startsWith("If true")) {
            currentMonkey.trueMonkey = parseInt(line.match(/\d+/)?.[0] ?? "");
        } else if (line.startsWith("If false")) {
            currentMonkey.falseMonkey = parseInt(line.match(/\d+/)?.[0] ?? "");
            monkeys.push(currentMonkey);
        }
    });

    const firstSet: Monkey[] = cloneDeep(monkeys);
    // Run 20 rounds for all monkeys
    performRounds(firstSet, 20, 3);
    // Return the multiplication of the inspectionCount of the 2 monkeys with the highest inspectionCount
    console.log(firstSet.map(x => x.inspectionCount).sort((a, b) => b - a).slice(0, 2).reduce((a, b) => a * b)); // part 1

    // Run 10000 rounds for all monkeys
    performRounds(monkeys, 10_000, 1);
    // Return the multiplication of the inspectionCount of the 2 monkeys with the highest inspectionCount
    console.log(monkeys.map(x => x.inspectionCount).sort((a, b) => b - a).slice(0, 2).reduce((a, b) => a * b)); // part 2
});

class Monkey {
    id: number = -1; // monkey id
    items: number[] = []; // objects carried by the monkey at each round. each item is also called "worry levels"
    inspectionCount: number = 0; // the number of times the monkey has inspected an item
    operationString: string[] = []; // sign and value, length 2
    testDivisibleValue: number = -1; // the value to test if the item is divisible by
    trueMonkey: number = -1; // the id of the monkey to give the item to if the test is true
    falseMonkey: number = -1; // the id of the monkey to give the item to if the test is false

    // Perform the operation on the item
    operation(old: number, stressDivisionFactor: number): number {
        const op = eval(`${old} ${this.operationString[0]} ${this.operationString[1].replace("old", `${old}`)}`);
        return stressDivisionFactor > 1 ? Math.floor(op / stressDivisionFactor) : op;
    }
}

function performRounds(monkeys: Monkey[], rounds: number, divisionFactor: number) {
    const lcmTestDivisibleValue = monkeys.map(x => x.testDivisibleValue).reduce((a, b) => lcm(a, b));

    for (let i = 0; i < rounds; i++) {
        for (const monkey of monkeys) {
            for (let item of monkey.items) {
                monkey.inspectionCount++;
                let operation = monkey.operation(item, divisionFactor);
                if (divisionFactor === 1) {
                    operation = operation % lcmTestDivisibleValue;
                }
                item = operation;
                if (item % monkey.testDivisibleValue === 0) {
                    monkeys[monkey.trueMonkey].items.push(item);
                } else {
                    monkeys[monkey.falseMonkey].items.push(item);
                }
            }
            monkey.items = [];
        }
    }
}
