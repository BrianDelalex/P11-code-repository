import { describe, expect, it } from "vitest"

import { find_nearest_hospital, get_hospitals_specialities } from '../Components/APIs/Hospital';


describe("integration suite", async () => {
    it("integration get_hospitals_specialities", async () => {
        const lists = await get_hospitals_specialities();

        expect(lists).not.toBe(undefined); 
    });

    it("integration find_nearest_hospital", async () => {
        let data = await find_nearest_hospital("Anesthésie", 50.39028759089191, -3.9204667072600907);

        expect.soft(data.statusCode).toBe(200);

        expect.soft(data.body.data).toBe("South Bristol Community Hospital");

        data = await find_nearest_hospital("Pédiatrie", 50.39028759089191, -3.9204667072600907)

        expect.soft(data.statusCode).toBe(200);

        expect.soft(data.body.data).toBe("Nuffield Health, Plymouth Hospital");
    });

})