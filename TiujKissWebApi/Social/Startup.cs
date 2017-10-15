using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(Social.Startup))]
namespace Social
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
