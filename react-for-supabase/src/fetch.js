import { createClient } from "@supabase/supabase-js";

// const supabaseUrl = process.env.NEXT_PUBLIC_SUPABASE_URL;
// const supabaseKey = process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY;
const supabaseUrl = "https://nsphmxetqzspfomxoljn.supabase.co";
const supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5zcGhteGV0cXpzcGZvbXhvbGpuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTAxNTI0MzMsImV4cCI6MjAyNTcyODQzM30.Bhw298b72GUjHmRKqVNGnHLybfgacNLw-r2FG4aSbPM";

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
