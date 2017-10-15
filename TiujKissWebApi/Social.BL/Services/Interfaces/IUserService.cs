using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using Social.BL.ViewModels;
using Social.BL.ViewModels.Api;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Social.BL.Services.Interfaces
{
    public interface IUserService
    {
        Task<IdentityResult> Register(RegisterViewModel registerModel);

        Task<SignInStatus> Login(LoginViewModel logInModel);

        Task<LoginApiViewModel> LoginApi(LoginApiGetViewModel logInModel);


    }
}
