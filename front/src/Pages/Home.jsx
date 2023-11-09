import { useEffect, useRef, useState } from 'react';
import GooglePlacesAutocomplete from 'react-google-places-autocomplete';
import Geocode from "react-geocode";
import { Toaster, toast } from 'react-hot-toast';
import BodyReader from '../Components/BodyReader';

let googleApiKey = import.meta.env.VITE_GOOGLE_API_KEY;
import './Home.css'
import { get_address_from_coords, get_coords_from_address } from '../Components/APIs/Google';
import { find_nearest_hospital, get_hospitals_specialities } from '../Components/APIs/Hospital';

Geocode.setLocationType("ROOFTOP");
Geocode.setApiKey(googleApiKey);

const Home = () => {
    console.log("googleApiKey", googleApiKey);
    const [location, setLocation] = useState(null);
    const [data, setData] = useState(undefined);
    const [specialities, setSpecialities] = useState([]);
    const [specialityGroups, setSpecialityGroups] = useState([]);
    const [selectedGroup, setSelectedGroup] = useState("");
    const [selectedSpeciality, setSelectedSpeciality] = useState("");
    const [selectableSpecialities, setSelectableSpecialities] = useState([]);
    const [result, setResult] = useState("");
    let groupSelectRef = useRef();
    let googlePlacesAutocompleteRef = useRef();

    const retrieveHospitalsSpecialities = async () => {
      const lists = await get_hospitals_specialities();
      if (lists == undefined)
        return;
      setSpecialities(lists.specialitiesList);
      setSelectableSpecialities(lists.specialitiesList);
      setSpecialityGroups(lists.groupList);
    };

    useEffect(() => {
      retrieveHospitalsSpecialities();
    }, []);

    const getLocation = () => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(getLocationSuccess, getLocationError);
        } else {
            console.log("Geolocation not supported");
            toast.error("Geolocation is not supported on your device");
        }
    }

    const getLocationSuccess = async (position) => {
        const latitude = position.coords.latitude;
        const longitude = position.coords.longitude;
        setLocation({ latitude, longitude });

        const address = await get_address_from_coords(latitude, longitude);
        if (address == undefined) {
          toast.error("We are unable to find your localization please enter it manually.");
        } else {
          setData(address)
        }

    }

    const getLocationError = (error) => {
        console.log(`Unable to retrieve your location ${error}`);
    }

    const OnGroupSelected = (evt) => {
      console.log("OnGroupSelected -> ", evt.currentTarget.value);
      setSelectedGroup(evt.currentTarget.value);
      const selectable = [];
      for (const obj of specialities) {
        console.log(`comp => ${obj.group} === ${evt.currentTarget.value}`);
        if (obj.group === evt.currentTarget.value) {
          selectable.push(obj);
          console.log(`equal`);
        }
      }
      setSelectableSpecialities(selectable);
    }

    const OnSpecialitySelected = (evt) => {
      console.log("OnSpecialitySelected -> ", evt.currentTarget.value);
      setSelectedSpeciality(evt.currentTarget.value);
      const index = specialities.findIndex((element) => element.speciality === evt.currentTarget.value);
      setSelectedGroup(specialities[index].group);
      groupSelectRef.current.value = specialities[index].group;
    }

    const getLocationFromAutocomplete = async (autocompleteData) => {
      console.log(autocompleteData);
      const coords = await get_coords_from_address(autocompleteData.label);
      if (coords != undefined)
        setLocation({latitude: coords.latitude, longitude: coords.longitude});
    }

    const onBtnSearch = async () => {
      console.log(location);
      if (location == null) return;
      const result = await find_nearest_hospital(selectedSpeciality, location.latitude, location.longitude);
      if (result.statusCode == 200)
        setResult(result.body.data);
    }

    return (
        <div className="windows">
            <div><Toaster/></div>
             <div className="containerInputLogin">
                <select ref={groupSelectRef} onChange={evt => OnGroupSelected(evt)} className="select" name="medicalGroup" id="medicalGroup">
                    <option value="">Please select a medical group type</option>
                    {specialityGroups.map((element, i ) => {
                      return (<option key={i} value={element}>{element}</option>);
                    })}
                </select>
                <select onChange={evt => OnSpecialitySelected(evt)} name="speciality" id="speciality">
                    <option value="">Please select speciality type</option>
                    {selectableSpecialities.map((element, i) => {
                        return (<option key={i} value={element.speciality}>{element.speciality}</option>);
                      })}
                </select>
                <div className='locationInput'>
                    <GooglePlacesAutocomplete 
                        ref={googlePlacesAutocompleteRef}
                        apiKey={googleApiKey} 
                        selectProps={{
                            inputValue: data,
                            defaultInputValue: data, //set default value
                            onChange: (data) => getLocationFromAutocomplete(data), //save the value gotten from google
                            placeholder: "Select your localization",
                        }}
                    />
                    <button className='locButton' onClick={getLocation}><img height='20' src="loc.png"/></button>
                </div>
                <button className="btnSubmit" onClick={onBtnSearch} > Search </button>
            </div>
            { result === "" ? null : 
              <div className='resultContainer'>
                <h1>Nearest hospital</h1>
                <h2>{result}</h2>
                <button className='reserveButton'>Reserve</button>
              </div>
            }
        </div>
    );
}

export default Home;