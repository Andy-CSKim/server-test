import { createClient } from "@supabase/supabase-js";

// in vite, we can use import.meta.env.VITE_SUPABASE_URL
const supabaseUrl = process.env.REACT_APP_SUPABASE_URL;
const supabaseKey = process.env.REACT_APP_SUPABASE_KEY;

const supabase = createClient(supabaseUrl, supabaseKey);

export const getUser = async () => {

  let { data, error } = await supabase
  .from('member')
  .select('*')

  if (error) return null;

  console.log(data);
  return data;

};

// newUser = {name: 'John', role: 'admin'}
export const postUser = async (newUser) => {
  const { data, error } = await supabase
  .from('member')
  .insert([
    newUser,
  ])
  .select()

  if (error) return null;

  console.log(data);
  return data;

};

// newUser = {name: 'John', role: 'admin'}
export const putUser = async (id, newUser) => {
  const { data, error } = await supabase
  .from('member')
  .update(newUser)
  .eq('id', id)
  .select()

  if (error) return null;

  console.log(data);
  return data;

};

export const deleteUser = async (id) => {
  const { error } = await supabase
  .from('member')
  .delete()
  .eq('id', id)

  if (error) return null;

  return 'OK';
}

// info
export const getInfo = async (userId) => {
  let { data, error } = await supabase
  .from('info')
  .select("*")
  // Filters
  .eq('user_id', userId)

  if (error) return null;

  console.log(data);
  return data;
}

export const postInfoAsString = async (userId, content) => {

  const newInfo = {content: `${content}`, user_id: `${userId}`};

  const { data, error } = await supabase
  .from('info')
  .insert([
    newInfo,
  ])
  .select()

  if (error) return null;

  console.log(data);
  return data;

}
