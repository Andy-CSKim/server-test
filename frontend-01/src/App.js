
import logo from './logo.svg';
import './App.css';
import {useState, useEffect, useRef} from 'react';
import {getUser, postUser, putUser, deleteUser, getInfo, postInfoAsString, postInfoAsObject} from './fetch';


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
  console.log(`React visits App() to render: name=${name.current}, role=${role.current}, id=${id.current}`)
  
  const doGet = () => {
    console.log("Get ==> ");

    getUser().then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp));
    });

  }

  const doPost = () => {
    console.log(`Post ==> name=${name.current}, role=${role.current}`)
    const newUser = {name: `${name.current}`, role: `${role.current}`};

    postUser(newUser).then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp));
    });
  }

  const doPut = () => {
    console.log(`Put ==> id=${id.current}, name=${name.current}, role=${role.current}`)
    const curUser = {name: `${name.current}`, role: `${role.current}`};

    putUser(id.current, curUser).then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp));
    });
  }

  const doDelete = () => {
    console.log(`Delete ==> id=${id.current}`);

    deleteUser(id.current).then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp));
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
      for (let obj of resp) {
        const tmp = JSON.parse(obj.content);
        console.log(tmp);
        // [0] : {id: 1, name: 'Lee', role: 'developer'}
        setDataFromServer(JSON.stringify(tmp));
      }
    });
  }

  const postJson = () => {
    const testObj = {first: [1, 2, 3, 4], second: {a: 1, b: 2, c: 3}};
    // const newInfo = {content: JSON.stringify(testObj), user_id: 1};

    const data = JSON.stringify(testObj);
    console.log(`Post JSON ==> ${data}`);

    postInfoAsString(id.current, data).then((resp) => {
      // server resposne is an array
      const obj = JSON.parse(resp.content);
      console.log(obj);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(obj));
    });
  }

  const postObject = () => {
    const testObj = {first: [1, 2, 3, 4], second: {a: 1, b: 2, c: 3}};
    // const newInfo = {content: testObj, user_id: 1};

    console.log(`Post JSON ==> ${testObj}`);

    postInfoAsObject(id.current, testObj).then((resp) => {
      // server resposne is an array
      const obj = JSON.parse(resp.content);
      console.log(obj);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(obj));
    });
  }


  return (
    <div className="App">
      <header className="App-header">
        <p>CRUD user example</p>
        <button onClick={doGet}>Get</button> <br/>
        <input type="text" name="name" placeholder="name" onInput={e => name.current = e.target.value}/>
        <input type="text" name="role" placeholder="role" onInput={e => role.current = e.target.value}/>
        <p>id is required only when put or delete</p>
        <input type="number" step="1" name="id" placeholder="id" onInput={e => id.current = e.target.value}/> <br/>
        <button onClick={doPost}>Post</button> <br/>
        <button onClick={doPut}>Put</button> <br/>
        <button onClick={doDelete}>Delete</button> <br/>

        <p>info example</p>
        <button onClick={getJson}>get JSON</button> <br/>
        <button onClick={postJson}>post JSON string</button> <br/>
        <button onClick={postObject}>post Object</button> <br/>        
        <br/><br/>
        <textarea name="postContent" rows={10} cols={50} value={dataFromServer}> </textarea>

      </header>
    </div>
  );
}

export default App;

