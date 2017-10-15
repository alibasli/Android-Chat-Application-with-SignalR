using Social.BL.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Security.Authentication;
using System.Text;
using System.Web.Http;
using System.Web.Http.Controllers;
using System.Web.Routing;

namespace Social.Areas.MobilApi.Controllers
{
    public abstract class BaseApiController : ApiController
    {
        //public readonly IUserService _userService;

        //public BaseApiController()
        //{
        //}

        //public BaseApiController(IUserService userService)
        //{
        //    _userService = userService;
        //}

        protected void Application_Start()
        {
        }

        public byte[] imageToByteArray(Image imageIn)
        {
            MemoryStream ms = new MemoryStream();
            imageIn.Save(ms, System.Drawing.Imaging.ImageFormat.Gif);
            return ms.ToArray();
        }

        public Image byteArrayToImage(byte[] byteArrayIn)
        {
            MemoryStream ms = new MemoryStream(byteArrayIn);
            Image returnImage = Image.FromStream(ms);
            return returnImage;
        }

        #region Authentication

        public bool CheckPassword(string sifre, string password)
        {
            var byt = Encoding.UTF8.GetBytes(password);
            var base64String = Convert.ToBase64String(byt);

            return base64String == sifre;
        }

        protected override void Initialize(HttpControllerContext controllerContext)
        {
            var authHeader = controllerContext.Request.Headers.Authorization;
            if (authHeader == null || authHeader.Scheme.ToLowerInvariant() != "basic")
                throw new AuthenticationException("Hatalı şema");

            var encodedUserPass = authHeader.Parameter.Trim();
            var userPass = Encoding.ASCII.GetString(Convert.FromBase64String(encodedUserPass));
            var parts = userPass.Split(":".ToCharArray());
            var email = parts[0];
            var password = parts[1].Trim();

            const string apiUser = "rimali@gmail.com";
            const string apiPassword = "rimaliResponse";

            if (apiPassword != password || apiUser != email)
                throw new AuthenticationException("Hatalı eposta yada şifre");

            base.Initialize(controllerContext);
        }

        #endregion
    }
}
