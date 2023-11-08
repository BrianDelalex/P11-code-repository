import BodyReader from "../BodyReader";

let googleApiKey = import.meta.env.VITE_GOOGLE_API_KEY;

export const get_address_from_coords = async (latitude, longitude) => {
    return await fetch(`https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude},${longitude}&location_type=ROOFTOP&key=${googleApiKey}`).then(
            async (data) => {
              let body = await BodyReader(data.body);
              body = JSON.parse(body);
              if (body.status === "ZERO_RESULTS") {
                return undefined;
              }
              const address = body.results[0].formatted_address;
              let city, state, country;
              for (let i = 0; i < body.results[0].address_components.length; i++) {
                for (let j = 0; j < body.results[0].address_components[i].types.length; j++) {
                  switch (body.results[0].address_components[i].types[j]) {
                    case "locality":
                      city = body.results[0].address_components[i].long_name;
                      break;
                    case "administrative_area_level_1":
                      state = body.results[0].address_components[i].long_name;
                      break;
                    case "country":
                      country = body.results[0].address_components[i].long_name;
                      break;
                  }
                }
              }
              console.log(city, state, country);
              console.log(address);
              return address;
            },
            (error) => {
              console.error(error);
              return undefined;
            });
}

export const get_coords_from_address = async (address) => {
    return await fetch(`https://maps.googleapis.com/maps/api/geocode/json?address=${address}&key=${googleApiKey}`).then(
        async (data) => {
          let body = await BodyReader(data.body);
          console.log(body);
          body = JSON.parse(body);
          if (body.results[0].geometry.location) {
            const latitude = body.results[0].geometry.location.lat;
            const longitude = body.results[0].geometry.location.lng;
            return ({latitude: latitude, longitude: longitude});
          }
        },
        (error) => {
            console.log("Error ", error);
            return undefined;
        }
    );
}