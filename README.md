# Audioshare

Project realized for the middelware class

## Communication models
- Client/Server : The server will have an object requested by user upon connection to advertise everyone that this user is online, and so on when the users disconnects
- Publish/Subcribe : A topic will be created on the server, to which the users can subscribe. Each user can filter what genre·s of music he wants to receive.
- Peer-to-peer : Each user can send a message on the queue of another user, assuming the sender know the username of the receiver.

## Technologies used
- RMI (Remote Method Invocation) : to handle notifications for each user connection & disconnection.
- JMS (Java Messaging System) : to handle messages for publish/subscribe and P2P models.

## Roadmap
- [ ] Sharing music on a topic
- [ ] Sharing music directly with another user
- [ ] Notifying users of connection and disconnection
OR
- [ ] Using JMS's acknowledgement system