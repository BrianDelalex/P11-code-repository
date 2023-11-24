import BodyReader from "../BodyReader";

export const get_hospitals_specialities = async () => {
    let url = import.meta.env.VITE_HOSPITAL_SERVICE_URL;
    url += "/hospital/specialities";
    return await fetch(url, {
      method: 'GET', // or 'PUT'
      mode: 'cors',
      Accept: '*/*',
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
      },
    }).then(async (data) => {
      let body = await BodyReader(data.body);

      const parsed = JSON.parse(body);
      let groupList = [];
      let specialitiesList = [];
      for (let obj of parsed.specialities) {
        groupList.push(obj.group);
        specialitiesList.push(obj);
      }
      groupList = [...new Set(groupList)];
      return {specialitiesList, groupList};

    }, (error) => {
      console.log("Error: " + error);
      return undefined;
    });
}

export const find_nearest_hospital = async (selectedSpeciality, latitude, longitude) => {
    let url = import.meta.env.VITE_HOSPITAL_SERVICE_URL;
    url += `/hospital?speciality=${selectedSpeciality}&latitude=${latitude}&longitude=${longitude}`;
    return await fetch(url, {
      headers: {
        "Content-Type": "application/json"
    }}).then( async (data) => {
      let body = await BodyReader(data.body);
      body = JSON.parse(body);
      return {statusCode: data.status, body};
    }, (error) => {
      console.log(error);
      return {statusCode: undefined, body: undefined};
    });
}