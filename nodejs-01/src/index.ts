import { PrismaClient } from "@prisma/client";
const prisma = new PrismaClient()

import fs from 'fs';
import express, { Request, Response } from 'express';
const cors = require('cors');

// const multer = require("multer");  // somthing wrong
// const upload = multer({ dest: "uploads/" });
// for parsing form data : doens't work
const formData = require("express-form-data");
const os = require("os");

const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: false })); // for parsing application/x-www-form-urlencoded

app.use(cors({
    origin: 'http://localhost:3000', // 모든 출처 허용 옵션. true 를 써도 된다.
    credentials: true,
}));

/**
 * Options are the same as multiparty takes.
 * But there is a new option "autoClean" to clean all files in "uploadDir" folder after the response.
 * By default, it is "false".
 */
const options = {
    uploadDir: os.tmpdir(),
    autoClean: true
  };
  
// parse data with connect-multiparty. 
app.use(formData.parse(options));
// delete from the request all empty files (size == 0)
app.use(formData.format());
// change the file objects to fs.ReadStream 
app.use(formData.stream());
// union the body and the files
app.use(formData.union());


// 🏚️ Default Route
// This is the Default Route of the API
app.get('/hello', async (req: Request, res: Response) => {
    res.json({ message: 'Hello World' });
});

// get length-path/{value}?unit=inch
// This is the Route for getting the length in inch
app.get('/length-path/:value', async (req: Request, res: Response) => {
    const { value } = req.params;
    const { unit } = req.query;
    if (unit === 'inch') {
        res.json({ message: "success", data: `${Number(value) / 2.54} inch` });
    }
    else if (unit === 'feet') {
        res.json({ message: "success", data: `${Number(value) / 30.48} feet` });
    } 
    else {
        res.json({ message: "success", data: "invalid unit" });
    }
});

// Get all users
// This is the Route for getting all users via GET Method
app.get('/users', async (req: Request, res: Response) => {
    // query infos together with the user and raw_data except content
    const users = await prisma.member.findMany(
        {
            include: {
                infos: true,
                raw_data: {
                    select: {
                        id: true,
                        file_type: true,
                        user_id: true
                    }
                }
            }
        }
    );
    // print object in console
    console.log('get /users' + JSON.stringify(users));
    // res.json({ data: users });
    res.json(users);
});

// Get single user
// This is the Route for getting a single user via GET Method
app.get('/users/:id', async (req: Request, res: Response) => {
    const { id } = req.params;
    const user = await prisma.member.findUnique({
        where: {
            id: Number(id)
        }
    });
    console.log('get /users/:id' + JSON.stringify(user));
    res.json(user);
});

// Create new user
// This is the Route for creating a new user via POST Method
app.post('/users', async (req: Request, res: Response) => {
    //get name and email from the request body
    const { name, role } = req.body;
    const user = await prisma.member.create({ 
        data: {
            name: String(name),
            role: String(role),
        }
    });
    console.log('post /users' + JSON.stringify(user));
    res.json(user);
});

// Update user with id
// This is the Route for updating a user via Patch Method
app.put('/users/:id', async (req: Request, res: Response) => {
    const { id } = req.params;
    const { name, role } = req.body;
    const user = await prisma.member.update({
        where: {
            id: Number(id)
        },
        data: {
            name: String(name),
            role: String(role)
        }
    });
    console.log('patch /users/:id' + JSON.stringify(user));
    res.json(user);
});

// Delete user with id
// This is the Route for deleting a user via DELETE Method
app.delete('/users/:id', async (req: Request, res: Response) => {
    const { id } = req.params;
    await prisma.member.delete({
        where: {
            id: Number(id)
        }
    });
    console.log('delete /users/:id');
    res.json("success");
});

// read /info/{user_id}
// This is the Route for reading a info via GET Method
app.get('/infos/:user_id', async (req: Request, res: Response) => {
    const { user_id } = req.params;
    const infos = await prisma.info.findMany({
        where: {
            user_id: Number(user_id)
        }
    });
    console.log('get /info/:user_id' + JSON.stringify(infos));
    res.json(infos);
});

// post /infos
// This is the Route for creating a new info via POST Method
app.post('/infos', async (req: Request, res: Response) => {
    const { content, user_id } = req.body;
    const info = await prisma.info.create({
        data: {
            content: String(content),
            user_id: Number(user_id)
        }
    });
    console.log('post /infos' + JSON.stringify(info));
    res.json(info);
});

// post /upload-bytes/{user_id}?file_type=image and body is form-data
// This is the Route for creating a new raw_data via POST Method
// app.post('/upload-bytes/:user_id', upload.single('file'), async (req: any, res: Response) => {
app.post('/upload-bytes/:user_id', async (req: any, res: Response) => {

    const { user_id } = req.params;
    const { fileType } = req.query;
    // file is form-data, body parser is required
    // const { file } = req.file;  // for multer
    const { file } = req.files;  // for express-form-data

    console.log(`req.file: ${JSON.stringify(file)}`);
    // console.log(`req.body: ${req.body}`);

    // read file as byte array
    let buffer = fs.readFileSync(file.path);

    // let buffer = Buffer.from(file, 'base64');
    //let buffer = Buffer.from(JSON.stringify(file), 'base64');    
    console.log(`buffer.length: ${buffer.length}`);
    console.log(`post /upload-bytes/${user_id}?fileType=${fileType}`);    


    // if raw_data exists, update it, otherwise create it
    const raw_data = await prisma.raw_data.findUnique({
        where: {
            user_id: Number(user_id)
        }
    });

    if (raw_data) {
        await prisma.raw_data.update({
            where: {
                id: raw_data.id
            },
            data: {
                file_type: String(fileType),
                content: Buffer.from(buffer)
            }
        });
    } else {
        await prisma.raw_data.create({
            data: {
                file_type: String(fileType),
                content: Buffer.from(buffer),
                user_id: Number(user_id)
            }
        });
    }
    res.json("OK");
});

// app.post('/upload-file/:user_id', upload.single('file'), async (req: any, res: Response) => {
app.post('/upload-file/:user_id', async (req: Request, res: Response) => {
    const { user_id } = req.params;
    // const { fileType } = req.query;
    const { fileType, file } = req.body;  // for express-form-data
    // const { fileType } = req.body; // for multer
    // const { file } = req.file;  // for multer
 
    console.log(`fileType ${fileType}`);    
    console.log(`file: ${JSON.stringify(file)}`);

    // read file as byte array
    let buffer = fs.readFileSync(file.path);

    console.log(`buffer.length: ${buffer.length}`);
    console.log(`post /upload-file/${user_id}` + fileType);

    // if raw_data exists, update it, otherwise create it
    const raw_data = await prisma.raw_data.findUnique({
        where: {
            user_id: Number(user_id)
        }
    });

    if (raw_data) {
        await prisma.raw_data.update({
            where: {
                id: raw_data.id
            },
            data: {
                file_type: String(fileType),
                content: Buffer.from(buffer)
            }
        });
    } else {
        await prisma.raw_data.create({
            data: {
                file_type: String(fileType),
                content: Buffer.from(buffer),
                user_id: Number(user_id)
            }
        });
    }
    res.json("OK");
});

// get /download-file/{user_id} and query file which can be image or video according to the file_type
// This is the Route for downloading a file via GET Method
app.get('/download-file/:user_id', async (req: Request, res: Response) => {
    const { user_id } = req.params;
    // const { file_type } = req.query;
    const raw_data = await prisma.raw_data.findFirst({
        where: {
            user_id: Number(user_id),
            // file_type: String(file_type)
        }
    });
    console.log(`get /download-file/:${user_id}, ` + raw_data?.file_type);
    //res.json(raw_data);
    // return raw_data.content which is either image or video according to raw_data.file_type
    if (raw_data) {
        // Determine the media type based on the file_type
        const mediaType = raw_data.file_type.startsWith('image') ? 'image/*' : 'audio/*';

        // Set the Content-Type header
        res.setHeader('Content-Type', mediaType);

        // Send the buffer directly
        res.send(raw_data.content);
    } else {
        res.status(404).json({ message: 'Data not found' });
    }
});

app.listen(3001, () => {
    console.log('Express server is running on port 3001');
});
