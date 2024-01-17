import axios from 'axios';

const BASE_URL = 'http://localhost:3001';

export const authApi = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
});

authApi.defaults.headers.common['Content-Type'] = 'application/json';

export const getUser = async () => {
  //const response = await authApi.get<QueryResponse>('query');
  try {
    const response = await authApi.get('/users');  // http://localhost:3100/users
    //console.log(response.data);
    return response.data;
  } catch (error) {
    console.log(error);
    return null;
  }
};

// newUser = {name: 'John', role: 'admin'}
export const postUser = async (newUser) => {
  //const response = await authApi.get<QueryResponse>('query');
  try {
    const response = await authApi.post('/users', newUser);  // http://localhost:3100/users
    //console.log(response.data);
    return response.data;
  } catch (error) {
    console.log(error);
    return null;
  }
};

// newUser = {name: 'John', role: 'admin'}
export const putUser = async (id, newUser) => {
  //const response = await authApi.get<QueryResponse>('query');
  try {
    const response = await authApi.put(`/users/${id}`, newUser);  // http://localhost:3100/users/6
    //console.log(response.data);
    return response.data;
  } catch (error) {
    console.log(error);
    return null;
  }
};

export const deleteUser = async (id) => {
  //const response = await authApi.get<QueryResponse>('query');
  try {
    const response = await authApi.delete(`/users/${id}`);  // http://localhost:3100/users/6
    //console.log(response.data);
    return response.data;
  } catch (error) {
    console.log(error);
    return null;
  }
}

// info
export const getInfo = async (id) => {
  //const response = await authApi.get<QueryResponse>('query');
  try {
    const response = await authApi.get(`/infos/${id}`);  // http://localhost:3100/infos
    //console.log(response.data);
    return response.data;
  } catch (error) {
    console.log(error);
    return null;
  }
}

export const postInfoAsString = async (id, data) => {

  const newInfo = {content: `${data}`, user_id: `${id}`};
  try {
    const response = await authApi.post('/infos', newInfo);  // http://localhost:3100/infos
    //console.log(response.data);
    return response.data;
  } catch (error) {
    console.log(error);
    return null;
  }
}

export const postInfoAsObject = async (id, data) => {
  //const response = await authApi.get<QueryResponse>('query');
  try {
    const response = await authApi.post(`/infos2/${id}`, data);  // http://localhost:3100/infos
    //console.log(response.data);
    return response.data;
  } catch (error) {
    console.log(error);
    return null;
  }
}

export const putInfo = async (id, newInfo) => {
  //const response = await authApi.get<QueryResponse>('query');
  try {
    const response = await authApi.put(`/infos/${id}`, newInfo);  // http://localhost:3100/infos/6
    //console.log(response.data);
    return response.data;
  } catch (error) {
    console.log(error);
    return null;
  }
}

export const deleteInfo = async (id) => {
  //const response = await authApi.get<QueryResponse>('query');
  try {
    const response = await authApi.delete(`/infos/${id}`);  // http://localhost:3100/infos/6
    //console.log(response.data);
    return response.data;
  } catch (error) {
    console.log(error);
    return null;
  }
}