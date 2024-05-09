# Chat Application

This is a Java-based chat application that supports text, image, and audio messaging, along with user accounts and interactive messaging features. The application utilizes sockets for network communication and `javax.sound.sampled` for audio handling, and integrates a SQLite database for managing user data.

## Features

- **Text Messaging**: Send and receive real-time text messages.
- **Image Messaging**: Share images within the chat. Supported formats include JPG.
- **Audio Messaging**: Record and play back audio messages. Audio recording is performed through the system's default microphone.
- **User Accounts**: Users can create accounts and log in, using the `JavaFinal.db` database.
- **Message Replies**: Users can directly reply to messages from other users.
- **Tagging Users**: Mention or tag other users in messages to draw their attention.

## Setup and Execution

1. **Clone the Repository**: Clone or download the repository to your local machine.
2. **Database Setup**: Ensure `JavaFinal.db` is located in the expected path or update the database connection string in the application code.
3. **Run the Server**: Navigate to the `ServerChat.java` file and execute it to start the server.
4. **Run the Client(s)**: Open one or more instances of `ClientChat.java` and execute them to start the client interfaces.
5. **Register or Log In**: Use the client interface to either register a new account or log in with existing credentials.
6. **Connect and Interact**: Start chatting, tagging, and replying within the chat interface.

## Important Notes

- **IDE Compatibility**: This application has been tested on Visual Studio Code. Eclipse does not support audio input from MacBook microphones, so it is recommended to use an alternative IDE if working on macOS and audio recording is needed.
- **Audio Configuration**: The application uses the system's default audio device for both input and output. Ensure your microphone and speakers are properly configured before running the application.
- **Network Configuration**: The application defaults to connecting to `localhost` on port `8888`. Verify that no other services are using this port before starting the server.
