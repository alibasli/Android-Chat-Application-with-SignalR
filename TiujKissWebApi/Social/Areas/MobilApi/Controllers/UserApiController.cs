using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Social.Areas.MobilApi.Controllers
{
    public class UserApiController : BaseApiController
    {      

        [HttpGet, Route("Api/Deneme")]
        public IHttpActionResult Deneme()
        {
            return Ok(new
            {
                Message = "Ok"
            });
        }
        [HttpPost, Route("Api/Login")]
        public IHttpActionResult Login(LoginApiModel data)
        {
            //var loginApiViewModel = _userService.LoginApi(data);

            //return Ok(loginApiViewModel);
            return Ok(data);
        }
        [HttpPost, Route("Api/Regiter")]
        public IHttpActionResult Regiter(RegisterApiModel data)
        {
            return Ok(new UserInfoApiModel
            {
                Id = 1,
                Name = "Ali",
                Surname = "Basli",
                Email = "ali@email.com",
                Birthday = "01-01-1990",
                Gender = "Male"
            });
        }
        [HttpPost, Route("Api/home")]
        public IHttpActionResult UserHome(UserIdModel data)
        {
            PostsApiModel post = new PostsApiModel();
            post.PostId = 1;
            post.PostOwnerId = 1;
            post.PostOwnerProfileImage = File.ReadAllBytes("C:/social/Social/Social/Content/indir.JPG");//imageToByteArray(image1);
            post.PostOwnerNameSurname = "ali basli";
            post.PostDateTime = "01.01.1990";
            post.PostTitle = "New Post";
            post.PostBody = "the post about being ADAM!";

            post.PostImages = new List<byte[]>();
            post.PostImages.Add(File.ReadAllBytes("C:/social/Social/Social/Content/indir.JPG"));

            List<PostsApiModel> posts = new List<PostsApiModel>();
            posts.Add(post);
            posts.Add(post);

            return Ok(posts);
        }
        [HttpPost, Route("Api/profile")]
        public IHttpActionResult UserProfile(UserIdModel data)
        {
            // kendi profil bilgileri ve kendi postları
            UserInfoApiModel userInfo = new UserInfoApiModel
            {
                Id = 1,
                Name = "Ali",
                Surname = "Basli",
                Email = "ali@email.com",
                Birthday = "01-01-1990",
                Gender = "Male"
            };
            PostsApiModel post = new PostsApiModel();
            post.PostId = 1;
            post.PostOwnerId = 1;
            post.PostOwnerProfileImage = File.ReadAllBytes("C:/social/Social/Social/Content/indir.JPG");//imageToByteArray(image1);
            post.PostOwnerNameSurname = "ali basli";
            post.PostDateTime = "01.01.1990";
            post.PostTitle = "New Post";
            post.PostBody = "the post about being ADAM!";
            post.PostImages = null;
            List<PostsApiModel> posts = new List<PostsApiModel>();
            posts.Add(post);
            posts.Add(post);

            return Ok(new
            {
                userInfo  = userInfo,
                userPosts = posts
            });
        }
        [HttpPost, Route("Api/homeMessages")]
        public IHttpActionResult UserHomeMessages(UserIdModel data)
        {
            MessageListModel msg = new MessageListModel
            {
                SenderId = 1,
                SenderNameSurname = "rdvn gns",
                MessageRead = true,
                SenderPhoto = File.ReadAllBytes("C:/social/Social/Social/Content/indir.JPG")
            };
            List<MessageListModel> messages = new List<MessageListModel>();
            messages.Add(msg);
            messages.Add(msg);
            messages.Add(msg);
            messages.Add(msg);
            messages.Add(msg);
            messages.Add(msg);
            messages.Add(msg);
            messages.Add(msg);
            messages.Add(msg);
            messages.Add(msg);
            messages.Add(msg);

            return Ok(messages);            
        }
        [HttpPost, Route("Api/feedback")]
        public IHttpActionResult UserFeedback(UserIdModel data)
        {
            FeedbackModel feedLike = new FeedbackModel {
                FeedId = 1,
                FeedBody = "liked",
                FeedDateTime = "01.01.1990",
                OwnerId = 1,
                OwnerNameSurname = "ali basli",
                OwnerPhoto = File.ReadAllBytes("C:/social/Social/Social/Content/indir.JPG"),
                PostId = 1,
                PostTitle = "first post"
        };
            FeedbackModel feedComment = new FeedbackModel
            {
                FeedId = 1,
                FeedBody = "Commented",
                FeedDateTime = "01.01.1990",
                OwnerId = 1,
                OwnerNameSurname = "ali basli",
                OwnerPhoto = File.ReadAllBytes("C:/social/Social/Social/Content/indir.JPG"),
                PostId = 1,
                PostTitle = "first post"
            };

            List<FeedbackModel> feeds = new List<FeedbackModel>();
            feeds.Add(feedLike);
            feeds.Add(feedComment);
            feeds.Add(feedLike);
            feeds.Add(feedComment);
            feeds.Add(feedLike);
            feeds.Add(feedComment);

            return Ok(feeds);
        }
        [HttpPost, Route("Api/photos")]
        public IHttpActionResult UserPhotos(UserIdModel data)
        {
            return Ok(new
            {
                Messsage = "Ok"
            });
        }
    }
}
