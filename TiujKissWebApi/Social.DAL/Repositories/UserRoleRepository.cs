using Social.DAL.Repositories.Interfaces;
using Social.DAL.Entities;
using Social.DAL.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Social.DAL.Repositories
{
    public class UserRoleRepository : Repository<UserRole>, IUserRoleRepository
    {
        public UserRoleRepository(ApplicationDbContext context)
            : base(context)
        {
        }
    }
}
