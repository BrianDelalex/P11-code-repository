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
      
      console.log("Result: " + body);
      const parsed = JSON.parse(body);
      let groupList = [];
      let specialitiesList = [];
      for (let obj of parsed.specialities) {
        console.log(JSON.stringify(obj));
        groupList.push(obj.group);
        specialitiesList.push(obj);
      }
      console.log("grouplist length ", groupList.length);
      groupList = [...new Set(groupList)];
      console.log("grouplist length ", groupList.length);
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
      console.log(data);
      let body = await BodyReader(data.body);
      body = JSON.parse(body);
      return {statusCode: data.status, body};
    }, (error) => {
      console.log(error);
      return {statusCode: undefined, body: undefined};
    });
}