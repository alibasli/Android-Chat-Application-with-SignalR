using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Social.DAL.Entities
{
   public class Following
    {
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid FollowingID { get; set; }

        public Guid FromUserID { get; set; }

        public Guid ToUserID { get; set; }

        public DateTime DateFollowing { get; set; }

        public bool Status { get; set; }

        public string Message { get; set; }

        public virtual User User { get; set; }

    }
}
