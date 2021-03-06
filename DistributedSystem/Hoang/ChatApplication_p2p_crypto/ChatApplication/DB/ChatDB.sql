USE [master]
GO
/****** Object:  Database [ChatAppDB]    Script Date: 11/9/2016 12:05:47 AM ******/
CREATE DATABASE [ChatAppDB]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'ChatAppDB', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL11.SQLEXPRESS\MSSQL\DATA\ChatAppDB.mdf' , SIZE = 4096KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'ChatAppDB_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL11.SQLEXPRESS\MSSQL\DATA\ChatAppDB_log.ldf' , SIZE = 1024KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [ChatAppDB] SET COMPATIBILITY_LEVEL = 110
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [ChatAppDB].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [ChatAppDB] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [ChatAppDB] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [ChatAppDB] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [ChatAppDB] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [ChatAppDB] SET ARITHABORT OFF 
GO
ALTER DATABASE [ChatAppDB] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [ChatAppDB] SET AUTO_CREATE_STATISTICS ON 
GO
ALTER DATABASE [ChatAppDB] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [ChatAppDB] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [ChatAppDB] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [ChatAppDB] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [ChatAppDB] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [ChatAppDB] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [ChatAppDB] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [ChatAppDB] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [ChatAppDB] SET  DISABLE_BROKER 
GO
ALTER DATABASE [ChatAppDB] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [ChatAppDB] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [ChatAppDB] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [ChatAppDB] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [ChatAppDB] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [ChatAppDB] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [ChatAppDB] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [ChatAppDB] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [ChatAppDB] SET  MULTI_USER 
GO
ALTER DATABASE [ChatAppDB] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [ChatAppDB] SET DB_CHAINING OFF 
GO
ALTER DATABASE [ChatAppDB] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [ChatAppDB] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
USE [ChatAppDB]
GO
/****** Object:  StoredProcedure [dbo].[SP_HISTORYINSERT]    Script Date: 11/9/2016 12:05:47 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_HISTORYINSERT] (
	@SENDER VARCHAR(20),
	@RECEIVER VARCHAR(20),
	@HISTORY NVARCHAR(1000),
	@DATESENT VARCHAR(20)
)
AS
BEGIN
	INSERT INTO History VALUES(@SENDER, @RECEIVER, @HISTORY, @DATESENT)
END
GO
/****** Object:  StoredProcedure [dbo].[SP_USERACCDELETE]    Script Date: 11/9/2016 12:05:47 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_USERACCDELETE] (
	@USERNAME VARCHAR(20)
)
AS
BEGIN
	DELETE FROM UserAccount WHERE username = @USERNAME
END
GO
/****** Object:  StoredProcedure [dbo].[SP_USERACCINSERT]    Script Date: 11/9/2016 12:05:47 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_USERACCINSERT] (
	@USERNAME VARCHAR(20),
	@PASSWORD VARCHAR(1000)
)
AS
BEGIN
	INSERT INTO UserAccount VALUES(@USERNAME, @PASSWORD)
END
GO
/****** Object:  StoredProcedure [dbo].[SP_USERACCUPDATE]    Script Date: 11/9/2016 12:05:47 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_USERACCUPDATE] (
	@USERNAME VARCHAR(20),
	@PASSWORD VARCHAR(1000)
)
AS
BEGIN
	UPDATE UserAccount SET password = @PASSWORD WHERE username = @USERNAME
END
GO
/****** Object:  Table [dbo].[History]    Script Date: 11/9/2016 12:05:47 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[History](
	[STT] [int] IDENTITY(1,1) NOT NULL,
	[Sender] [varchar](20) NOT NULL,
	[Receiver] [varchar](20) NOT NULL,
	[History] [nvarchar](1000) NOT NULL,
	[DateSent] [varchar](20) NOT NULL,
 CONSTRAINT [PK_History] PRIMARY KEY CLUSTERED 
(
	[STT] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[UserAccount]    Script Date: 11/9/2016 12:05:47 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[UserAccount](
	[username] [varchar](20) NOT NULL,
	[password] [varchar](1000) NOT NULL,
 CONSTRAINT [PK_UserAccount] PRIMARY KEY CLUSTERED 
(
	[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
SET IDENTITY_INSERT [dbo].[History] ON 

INSERT [dbo].[History] ([STT], [Sender], [Receiver], [History], [DateSent]) VALUES (32, N'user1', N'user2', N'user1: hi
', N'11/08/2016 23:58:57')
INSERT [dbo].[History] ([STT], [Sender], [Receiver], [History], [DateSent]) VALUES (33, N'user2', N'user1', N'user2: how are you?
', N'11/08/2016 23:59:00')
SET IDENTITY_INSERT [dbo].[History] OFF
INSERT [dbo].[UserAccount] ([username], [password]) VALUES (N'admin', N'12345')
INSERT [dbo].[UserAccount] ([username], [password]) VALUES (N'user1', N'12345')
INSERT [dbo].[UserAccount] ([username], [password]) VALUES (N'user2', N'12345')
INSERT [dbo].[UserAccount] ([username], [password]) VALUES (N'user3', N'12345')
ALTER TABLE [dbo].[History]  WITH CHECK ADD  CONSTRAINT [FK_History_UserAccount] FOREIGN KEY([Sender])
REFERENCES [dbo].[UserAccount] ([username])
GO
ALTER TABLE [dbo].[History] CHECK CONSTRAINT [FK_History_UserAccount]
GO
ALTER TABLE [dbo].[History]  WITH CHECK ADD  CONSTRAINT [FK_History_UserAccount1] FOREIGN KEY([Receiver])
REFERENCES [dbo].[UserAccount] ([username])
GO
ALTER TABLE [dbo].[History] CHECK CONSTRAINT [FK_History_UserAccount1]
GO
USE [master]
GO
ALTER DATABASE [ChatAppDB] SET  READ_WRITE 
GO
