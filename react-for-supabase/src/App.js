
import logo from './logo.svg';
import './App.css';
import {useState, useEffect, useRef} from 'react';
import {getUser, postUser, putUser, deleteUser, getInfo, postInfoAsString} from './fetch';

// function app() : what's the difference between function app() and function App()?
// App() is a graphic component, app() is a function

function App() {
  // use dataFromServer when reading, use setDataFromServer(value) when writing
  // React will call App() every time dataFromServer is changed
  const [dataFromServer, setDataFromServer] = useState() // empty array
  // change to useRef() to call once
  //const [name, setName] = useState("")
  // const [role, setRole] = useState("")
  // const [id, setId] = useState(0)

  // use name.current when both reading and writing
  // React won't call App() when name.current is changed
  const name = useRef("")
  const role = useRef("")
  const id = useRef(0)

  let count = 0;

  // for checking how many times App() is called
  //console.log(`React visits App() to render: name=${name.current}, role=${role.current}, id=${id.current}`)
  
  const doGet = () => {
    console.log("Get ==> ");

    getUser().then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp, undefined, 2));
    });

  }

  const doPost = () => {
    console.log(`Post ==> name=${name.current}, role=${role.current}`)
    const newUser = {name: `${name.current}`, role: `${role.current}`};

    postUser(newUser).then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp, undefined, 2));
    });
  }

  const doPut = () => {
    console.log(`Put ==> id=${id.current}, name=${name.current}, role=${role.current}`)
    const curUser = {name: `${name.current}`, role: `${role.current}`};

    putUser(id.current, curUser).then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp, undefined, 2));
    });
  }

  const doDelete = () => {
    console.log(`Delete ==> id=${id.current}`);

    deleteUser(id.current).then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp, undefined, 2));
    });
  }  

  const getJson = () => {
    console.log(`Get JSON ==> id=${id.current}`);

    getInfo(id.current).then((resp) => {
      // server resposne is an array
      if (resp == null) {
        console.log("resp is null");
        return;
      }
      console.log(resp);
      setDataFromServer(JSON.stringify(resp, undefined, 2));
      // for (let obj of resp) {
      //   const tmp = JSON.parse(obj.content);
      //   console.log(tmp);
      //   // [0] : {id: 1, name: 'Lee', role: 'developer'}
      //   setDataFromServer(JSON.stringify(tmp, undefined, 2));
      // }
    });
  }

  const postJson = () => {
    const testObj = {first: [1, 2, 3, 4], second: {a: 1, b: 2, c: 3}};
    // const newInfo = {content: JSON.stringify(testObj), user_id: 1};

    const content = JSON.stringify(testObj);
    console.log(`Post json ==> ${content}`);

    postInfoAsString(id.current, content).then((resp) => {

      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp, undefined, 2));

      // server resposne is an array
      // const obj = JSON.parse(resp.content);
      // console.log(obj);
    });
  }


  return (
    <div className="App">
      <header className="App-header">
        <p>CRUD user example</p>
        <button onClick={doGet}>Get users</button> <br/>
        <input type="text" name="name" placeholder="name" onInput={e => name.current = e.target.value}/>
        <input type="text" name="role" placeholder="role" onInput={e => role.current = e.target.value}/> <br/>
        <button onClick={doPost}>Post a user</button> <br/>

        <p>id is also required when put or delete</p>
        <input type="number" step="1" name="id" placeholder="id" onInput={e => id.current = e.target.value}/> <br/>
        <button onClick={doPut}>Put the user</button> <br/>
        <button onClick={doDelete}>Delete the user</button> <br/>

        <p>info example (user id is required)</p>
        <button onClick={getJson}>get JSON</button> <br/>
        <button onClick={postJson}>post JSON string</button> <br/>    
        <br/><br/>
        <textarea name="postContent" rows={100} cols={100} value={dataFromServer}> </textarea>

      </header>
    </div>
  );
}

export default App;

