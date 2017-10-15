using Social.BL.Services.Interfaces;
using Social.BL.ViewModels;
using Social.DAL.Entities;
using Social.DAL.Repositories.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.AspNet.Identity;
using Social.BL.ViewModels.Api;
using Social.DAL.Entities.Enums;

namespace Social.BL.Services
{
    public class UserService : IUserService
    {
        private readonly IUserRepository _userRepository;
        private readonly IRoleRepository _roleRepository;

        private UserManager _userManager;
        private SignInManager _signInManager;
        private RoleManager _roleManager;

        public UserManager UserManager
        {
            get
            {
                return _userManager ?? HttpContext.Current.GetOwinContext().GetUserManager<UserManager>();
            }
            private set
            {
                _userManager = value;
            }
        }

        public SignInManager SignInManager
        {
            get
            {
                return _signInManager ?? HttpContext.Current.GetOwinContext().GetUserManager<SignInManager>();
            }
            private set
            {
                _signInManager = value;
            }
        }

        public RoleManager RoleManager
        {
            get
            {
                return _roleManager ?? HttpContext.Current.GetOwinContext().Get<RoleManager>();
            }
            private set { _roleManager = value; }
        }

        public UserService(IUserRepository userRepository, IRoleRepository roleRepository)
        {
            _userRepository = userRepository;
            _roleRepository = roleRepository;

        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="registerModel"></param>
        /// <returns></returns>
        public async Task<IdentityResult> Register(RegisterViewModel registerModel)
        {
            var user = new User
            {
                UserName = registerModel.Email,
                Email = registerModel.Email,
                Name = registerModel.Name,
                Lastname = registerModel.Lastname,
                DateCreated = DateTime.Now,
                Modified = DateTime.Now,
                LastLogin = DateTime.Now,
                BirthDate = DateTime.Now,
                IsActive = true,
            };
            
            var result = await UserManager.CreateAsync(user, registerModel.Password);
            registerModel.UserID = user.Id;

            if (result.Succeeded)
            {
                //if (!RoleManager.RoleExists("User"))
                //{
                //    var role = new Role();
                //    role.Name = "User";
                //    RoleManager.Create(role);
                //}

                UserManager.AddToRole(user.Id, "User");
                await SignInManager.SignInAsync(user, isPersistent: false, rememberBrowser: false);
            }

            return result;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="registerModel"></param>
        /// <returns></returns>
        public async Task<SignInStatus> Login(LoginViewModel logInModel)
        {
            var result = await SignInManager.PasswordSignInAsync(logInModel.Email, logInModel.Password, logInModel.RememberMe, shouldLockout: false);

            switch (result)
            {
                case SignInStatus.Success:
                    var user = await UserManager.FindByNameAsync(logInModel.Email);
                    if (user != null)
                    {
                        if (await UserManager.IsEmailConfirmedAsync(user.Id))
                            LoginDetail(user);
                    }
                    break;
            }

            return result;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="registerModel"></param>
        /// <returns></returns>
        public async Task<LoginApiViewModel> LoginApi(LoginApiGetViewModel logInModel)
        {
            LoginApiViewModel loginApiViewModel = new LoginApiViewModel();
            var result = await SignInManager.PasswordSignInAsync(logInModel.Email, logInModel.Password, logInModel.RememberMe, shouldLockout: false);

            switch (result)
            {
                case SignInStatus.Success:
                    var user = await UserManager.FindByNameAsync(logInModel.Email);
                    if (user != null)
                    {
                        if (await UserManager.IsEmailConfirmedAsync(user.Id))
                            LoginDetail(user);
                        loginApiViewModel.BirthDate = user.BirthDate;
                        loginApiViewModel.Email = user.Email;
                        loginApiViewModel.Gender = user.Gender;
                        loginApiViewModel.LastName = user.Lastname;
                        loginApiViewModel.Name = user.Name;
                        loginApiViewModel.UserID = user.Id;
                        loginApiViewModel.LoginErrorStatus=LoginErrorStatus.Success,
                    }
                    break;
            }

            return loginApiViewModel;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="user"></param>
        private void LoginDetail(User user)
        {
            if (user.IsActive == true)
            {
                List<UserRole> roles = user.Roles.ToList();
                List<string> rolelist = new List<string>();

                HttpContext.Current.Session["LoginID"] = user.Id;
                HttpContext.Current.Session["LoginImage"] = user.ProfilePhoto;

                if (user.CoverPhoto == null)
                    HttpContext.Current.Session["CoverImage"] = @"\Content\img\profile-menu.png";
                else
                    HttpContext.Current.Session["CoverImage"] = user.CoverPhoto;

                HttpContext.Current.Session["LoginName"] = user.Name + " " + user.Lastname;

                foreach (var itemrole in roles)
                {
                    Role role = _roleRepository.FirstOrDefault(a => a.Id == itemrole.RoleId);
                    rolelist.Add(role.Name);
                }

                HttpContext.Current.Session["Roles"] = rolelist;

                foreach (string role in rolelist)
                {
                    if (role == "Admin")
                        HttpContext.Current.Session["Admin"] = "true";
                    if (role == "SkiCenterAdmin")
                        HttpContext.Current.Session["SkiCenterAdmin"] = "true";
                    if (role == "Teacher")
                        HttpContext.Current.Session["Teacher"] = "true";
                }
            }
        }




    }
}