
import logo from './logo.svg';
import './App.css';
import {useState, useEffect, useRef} from 'react';
import {getUser, postUser, putUser, deleteUser, getInfo, postInfoAsString, postInfoAsObject, sendBytes, sendFile, rcvFile} from './fetch';

import ReactAudioPlayer from 'react-audio-player';

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

  const userId = useRef(0)
  const contents = useRef("")

  const fileInput = useRef(null);
  const [rawData, setRawData] = useState([]);

  // const audioOrImage = useRef("image");
  const [audioOrImage, setAudioOrImage] = useState("image");

  let count = 0;

  // for checking how many times App() is called
  console.log(`React visits App() to render: name=${name.current}, role=${role.current}, id=${id.current} audioOrImage=${audioOrImage}`)
  
  const doGet = () => {
    console.log("Get ==> ");

    getUser().then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp, undefined, 2));
    })
    .catch((error) => {
      console.log(error);
      setDataFromServer("Read users error!");
    });

  }

  const doPost = () => {
    console.log(`Post ==> name=${name.current}, role=${role.current}`)
    // json follows snake case
    const newUser = {name: `${name.current}`, role: `${role.current}`};

    postUser(newUser).then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp, undefined, 2));
    })
    .catch((error) => {
      console.log(error);
      setDataFromServer("Create user error!");
    });
  }

  const doPut = () => {
    console.log(`Put ==> id=${id.current}, name=${name.current}, role=${role.current}`)
    // json follows snake case
    const curUser = {name: `${name.current}`, role: `${role.current}`};

    if (id.current == 0) {
      alert("id is required");
      return;
    }

    putUser(id.current, curUser).then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp, undefined, 2));
    })
    .catch((error) => {
      console.log(error);
      setDataFromServer("Update user error!");
    });
  }

  const doDelete = () => {
    console.log(`Delete ==> id=${id.current}`);

    if (id.current == 0) {
      alert("id is required");
      return;
    }    

    deleteUser(id.current).then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp, undefined, 2));
    })
    .catch((error) => {
      console.log(error);
      setDataFromServer("Delete user error!");
    });
  }  


  const getContents = () => {
    console.log(`Get contents ==> id=${userId.current}`);

    getInfo(userId.current).then((resp) => {
      // server resposne is an array
      if (resp == null) {
        console.log("resp is null");
        setDataFromServer(`no contents`);
        return;
      }
      console.log(resp);
      setDataFromServer(JSON.stringify(resp, undefined, 2));
    });
  } 

  const postContent = () => {
    console.log(`Post Contents ==> user id=${userId.current}, contents=${contents.current}`);

    if (userId.current == 0) {
      alert("user id is required");
      return;
    }

    postInfoAsString(userId.current, contents.current).then((resp) => {
      // server resposne is an array
      if (resp == null) {
        console.log("resp is null");
        setDataFromServer(`create content error!`);
        return;
      }
      console.log(resp);
      setDataFromServer(JSON.stringify(resp, undefined, 2));
    });
  }

  // test code
  const getJson = () => {
    console.log(`Get JSON ==> user id=${userId.current}`);

    getInfo(userId.current).then((resp) => {
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
    const testObj = {list: [1, 2, 3, 4], dictionary: {a: 1, b: 2, c: 3}};
    // const newInfo = {content: JSON.stringify(testObj), user_id: 1};

    const data = JSON.stringify(testObj);
    console.log(`Post json ==> ${data}`);

    postInfoAsString(userId.current, data).then((resp) => {
      // server resposne is an array
      const obj = JSON.parse(resp.content);
      console.log(obj);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(obj, undefined, 2));
    });
  }

  const postObject = () => {
    const testObj = {first: [1, 2, 3, 4], second: {a: 1, b: 2, c: 3}};
    // const newInfo = {content: testObj, user_id: 1};

    console.log(`Post Object ==> ${testObj}`);

    postInfoAsObject(userId.current, testObj).then((resp) => {
      // server resposne is an array
      const obj = JSON.parse(resp.content);
      console.log(obj);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(obj, undefined, 2));
    });
  }

  const uploadHandler = () => {
    console.log(audioOrImage, fileInput.current);

    sendBytes(userId.current, audioOrImage, fileInput.current).then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp, undefined, 2));
    });
    
  }

  const uploadFileHandler = () => {
    console.log(audioOrImage, fileInput.current);

    sendFile(userId.current, audioOrImage, fileInput.current).then((resp) => {
      // server resposne is an array
      console.log(resp);
      // [0] : {id: 1, name: 'Lee', role: 'developer'}
      setDataFromServer(JSON.stringify(resp, undefined, 2));
    });
    
  }

  const downloadFileHandler = () => {
    console.log("Download file ==> ");

    rcvFile(userId.current).then((resp) => {
      // [0] : {id: 1, name: 'Lee', role: 'developer'}

      if (resp == null) {
        console.log("resp is null");
        setDataFromServer(`no raw data`);
        setRawData([]);
        return;
      }

      setDataFromServer(`size of raw data: ${resp.size}bytes`);
      // server resposne is an array
      console.log(resp);
      // check the type of resp and set Image or Audio
      if (resp.type.includes("audio")) {
        setAudioOrImage("audio");
      } else {
        setAudioOrImage("image");
      }
      setRawData(URL.createObjectURL(resp));
    });

  }

  return (
    <div className="App">
      {/* <header className="App-header"> */}
        <p>CRUD example</p>
        <button onClick={doGet}>get users</button> <br/>
        <input type="text" name="name" placeholder="name" onInput={e => name.current = e.target.value}/>
        <input type="text" name="role" placeholder="role" onInput={e => role.current = e.target.value}/> &nbsp;&nbsp;&nbsp;&nbsp;
        <button onClick={doPost}>post a user</button> <br/>

        <input type="number" step="1" name="id" placeholder="id" onInput={e => id.current = e.target.value}/> &nbsp;&nbsp;&nbsp;&nbsp;
        <button onClick={doPut}>put(update) the user</button>  &nbsp;
        <button onClick={doDelete}>delete the user</button>
        <p>*id above is required when put(update) or delete</p>

        <p>user(1) : info(N) CRUD example (with user id below)</p>
        <input type="number" step="1" name="user id" placeholder="user id" onInput={e => userId.current = e.target.value}/> &nbsp;&nbsp;&nbsp;&nbsp;
        <button onClick={getContents}>get contents</button> <br/>
        <input type="text" name="content" placeholder="content" onInput={e => contents.current = e.target.value}/> &nbsp;&nbsp;&nbsp;&nbsp;
        <button onClick={postContent}>post a content</button>
        {/* <button onClick={postObject}>post Object</button>         */}
        <br/>

        <p>user(1) : file(1) example (with user id above)</p>
        <input type="radio" name="fileType" value="audio" checked={audioOrImage!=="image"} onChange={e => setAudioOrImage(e.target.value) } /> audio
        <input type="radio" name="fileType" value="image" checked={audioOrImage==="image"} onChange={e => setAudioOrImage(e.target.value) } /> image  &nbsp;&nbsp;&nbsp;&nbsp;

        <div>
          {audioOrImage === "image"
            ? <input type="file" id="file" name="file" accept="image/*" ref={fileInput} onChange={e => fileInput.current = e.target.files[0]} />
            : <input type="file" id="file" name="file" accept="audio/*" ref={fileInput} onChange={e => fileInput.current = e.target.files[0]} />
          }
        </div>
        <button onClick={uploadHandler}>upload as byte</button> &nbsp;&nbsp;
        <button onClick={uploadFileHandler}>upload as multipart files</button> &nbsp;&nbsp;
        <button onClick={downloadFileHandler}>read raw data</button>

        <br/><br/>
        <textarea name="postContent" rows={40} cols={100} value={dataFromServer}> </textarea>
        <br/><br/>
        <div>
          {audioOrImage === "image" 
            ? <img src={`${rawData}`} width={400} height={400} alt="logo"/> 
            : <ReactAudioPlayer src={`${rawData}`} controls/>}
        </div>


      {/* </header> */}
    </div>
  );
}

export default App;

