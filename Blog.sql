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
   type varchar(10)
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
                                                                                                                              

