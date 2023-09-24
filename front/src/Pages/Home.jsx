import { useEffect, useState } from 'react';
import GooglePlacesAutocomplete from 'react-google-places-autocomplete';
import Geocode from "react-geocode";
import { Toaster, toast } from 'react-hot-toast';
import BodyReader from '../Components/BodyReader';

let googleApiKey = import.meta.env.VITE_GOOGLE_API_KEY;
import './Home.css'

Geocode.setLocationType("ROOFTOP");
Geocode.setApiKey(googleApiKey);

const Home = () => {
    console.log("googleApiKey", googleApiKey);
    const [location, setLocation] = useState(null);
    const [data, setData] = useState(undefined);
    const [autocompleteData, setAutocompleteData] = useState(undefined);
    const [specialities, setSpecialities] = useState("");
    const [specialityGroups, setSpecialityGroups] = useState([]);

    useEffect(() => {
      let url = import.meta.env.VITE_HOSPITAL_SERVICE_URL;
      url += "/hospital/specialities";
      fetch(url, {
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
        let specialitiesList = [];
        let groupList = [];
        for (let obj of parsed.specialities) {
          console.log(JSON.stringify(obj));
          specialitiesList.push(obj.speciality);
          groupList.push(obj.group);
        }
        console.log("grouplist length ", groupList.length);
        groupList = [...new Set(groupList)];
        console.log("grouplist length ", groupList.length);
        setSpecialities(specialitiesList);
        setSpecialityGroups(groupList);
      }, (error) => {
        console.log("Error: " + error);
      });
    }, []);

    function getLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(success, error);
        } else {
            console.log("Geolocation not supported");
        }
    }

    function success(position) {
        const latitude = position.coords.latitude;
        const longitude = position.coords.longitude;
        const utf8Decoder = new TextDecoder("utf-8");
        setLocation({ latitude, longitude });
        console.log(`Latitude: ${latitude}, Longitude: ${longitude}`);
        //Geocode.fromLatLng(latitude,longitude, googleApiKey)
        fetch(`https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude},${longitude}&location_type=ROOFTOP&key=${googleApiKey}`).then(
            async (data) => {
              console.log("HERE");
              console.log(data);
              let body = await BodyReader(data.body);
              console.log("Result: " + body);
              body = JSON.parse(body);
              if (body.status === "ZERO_RESULTS") {
                toast.error("We are unable to find your localization please enter it manually.");
                return;
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
              setData(address)
            },
            (error) => {
              console.error(error);
            });
    }

    function error() {
        console.log("Unable to retrieve your location");
    }

    return (
        <div className="windows">
            <div><Toaster/></div>
             <div className="containerInputLogin">
                <select className="select" name="medicalGroup" id="medicalGroup">
                    <option value="">Please select a medical group type</option>
                    {specialityGroups.map((element, i ) => {
                      return (<option key={i} value={element}>{element}</option>);
                    })}
                </select>
                <select name="speciality" id="speciality">
                    <option value="">Please select speciality type</option>
                </select>
                <div className='locationInput'>
                    <GooglePlacesAutocomplete 
                        apiKey={googleApiKey} 
                        selectProps={{
                            inputValue: data,
                            defaultInputValue: data, //set default value
                            onChange: setAutocompleteData, //save the value gotten from google
                            placeholder: "Select your localization",
                        }}
                    />
                    <button className='locButton' onClick={getLocation}><img className="locImage" src="loc.png"/></button>
                </div>
                <button className="btnSubmit" > Search </button>
            </div>
        </div>
    );
}

export default Home;