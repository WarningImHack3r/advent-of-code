import { readFile } from "fs";

readFile("2022/inputs/input19.txt", (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const input = data.toString().split("\n").filter(x => x.length > 0);
});
