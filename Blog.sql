Create database blog
go
use blog
go

Create table Users(
   ID int  IDENTITY(1,1) PRIMARY KEY,
   Username varchar(100) ,
   Pass varchar(100)  ,
   GioiTinh nvarchar(5),
   DiaChi nvarchar(200),
   Image varchar(200),
   SDT nvarchar(15),
   Email varchar(100)
)
Create table Conversations(
   ID int  IDENTITY(1,1) PRIMARY KEY,
   ConversationName  nvarchar(255),
   type varchar(10),
   create_at datetime
)
CREATE TABLE UserConversations(
   ID INT IDENTITY(1,1) PRIMARY KEY, 
   UserID INT NOT NULL,
   ConversationID INT NOT NULL,
   CONSTRAINT FK_UserID FOREIGN KEY(UserID) REFERENCES Users(ID),
   CONSTRAINT FK_ConversationID FOREIGN KEY(ConversationID) REFERENCES Conversations(ID),
   CONSTRAINT UC_UserConversation UNIQUE(UserID, ConversationID) -- Đảm bảo cặp UserID và ConversationID là duy nhất
);
--CREATE TABLE UserConversations(
--   UserID INT,
--   ConversationID INT,
--   CONSTRAINT FK_UserID FOREIGN KEY(UserID) REFERENCES Users(ID),
--   CONSTRAINT FK_ConversationID FOREIGN KEY(ConversationID) REFERENCES Conversations(ID), 
--   PRIMARY KEY(UserID, ConversationID)
--);  


create table Messages(
   ID int  IDENTITY(1,1)PRIMARY KEY,
   MessageText nvarchar(1000),
   MessageType varchar(100),
   Timestamp datetime,
   IdConversations int,
   IdUser int,
   CONSTRAINT FK_IdConvensations FOREIGN KEY(IdConversations) REFERENCES Conversations(ID),
    CONSTRAINT FK_IdUser FOREIGN KEY(IdUser) REFERENCES Users(ID),
)

create table otp(
    ID int  IDENTITY(1,1) PRIMARY KEY,
	email varchar(100),
	otp varchar(7),
	username varchar(100),
	create_at datetime,
	expire_at datetime
)

Create table Attrachment(
   ID int  IDENTITY(1,1)PRIMARY KEY,
   IdMessages int,
   AttrachmentType varchar(50)
   CONSTRAINT FK_IdMessages FOREIGN KEY(IdMessages) REFERENCES Messages(ID),
)
Create table Notifications(
   ID int  IDENTITY(1,1)PRIMARY KEY,
   NotificationMessage nvarchar(1000),
   ISRead bit
)
                                                                                                                              
INSERT INTO Users (Username, Pass, GioiTinh, DiaChi, SDT,Email) 
VALUES
('huept', '1601', N'Nữ', N'Thanh Hóa', '0336450133','hueptph36756@fpt.edu.vn'),
('tuannq', '0000', N'Nam', N'Hà Nội', '0336434795','huethipham1601@gmail.com'),
 ('vanlt', '0000', N'Nam', N'Hà Nội', '0336434795','phamthihue3340@gmail.com')

-- Insert a conversation (assuming an individual chat for now)
INSERT INTO Conversations (ConversationName, type,create_at) 
VALUES ('Chat between User1 and User2', 'private',GETDATE());

INSERT INTO Conversations (ConversationName, type,create_at) 
VALUES ('Group chat', 'group',GETDATE());

-- Map users to this conversation
INSERT INTO UserConversations (UserID, ConversationID) 
VALUES (1, 1), (2, 1);
INSERT INTO UserConversations (UserID, ConversationID) 
VALUES  (2, 2),(3,2);

-- Insert messages into the conversation
INSERT INTO Messages (MessageText, MessageType, Timestamp, IdConversations, IdUser) 
VALUES 
('Hello, how are you?', 'text', GETDATE(), 1, 1),
('I am fine, thank you!', 'text', GETDATE(), 1, 2);

INSERT INTO Messages (MessageText, MessageType, Timestamp, IdConversations, IdUser) 
VALUES 
('hi, how are you?', 'text', GETDATE(), 2, 1),
('Reply', 'text', GETDATE(), 2, 2),
('Im fine and you', 'text', GETDATE(), 2, 1);

INSERT INTO Messages (MessageText, MessageType, Timestamp, IdConversations, IdUser) 
VALUES 
('happy new year', 'text', GETDATE(), 4, 2),

SELECT C.* FROM Conversations C 
JOIN UserConversations UC ON C.ID = UC.ConversationID 
JOIN Users U ON UC.UserID = U.ID 
WHERE U.Username = 'huept';
select * from Users
select * from conversations
select * from UserConversations
select * from Messages where Messages.IdConversations = 1
drop table Notifications
drop table Attrachment
drop table Messages
drop table UserConversations
drop table Conversations
drop table Users
drop table OTP
