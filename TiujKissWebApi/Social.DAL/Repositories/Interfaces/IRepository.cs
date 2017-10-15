using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;

namespace Social.DAL.Repositories.Interfaces
{

    public interface IRepository<TEntity> where TEntity : class
    {
        //Get Entity
        TEntity Get(Guid id);
        Task<TEntity> GetAsync(Guid id);
        TEntity Single(Expression<Func<TEntity, bool>> predicate);
        Task<TEntity> SingleAsync(Expression<Func<TEntity, bool>> predicate);
        TEntity FirstOrDefault();
        Task<TEntity> FirstOrDefaultAsync(Guid id);
        TEntity FirstOrDefault(Expression<Func<TEntity, bool>> predicate);
        Task<TEntity> FirstOrDefaultAsync(Expression<Func<TEntity, bool>> predicate);
        TEntity Load(Guid id);

        //Get Entities (List)
        IEnumerable<TEntity> GetAll();
        Task<List<TEntity>> GetAllAsync();
        IEnumerable<TEntity> GetAll(Expression<Func<TEntity, bool>> predicate);
        Task<List<TEntity>> GetAllAsync(Expression<Func<TEntity, bool>> predicate);

        //Insert Entity
        TEntity Insert(TEntity entity);
        Task<TEntity> InsertAsync(TEntity entity);
        Guid InsertAndGetId(TEntity entity);
        Task<Guid> InsertAndGetIdAsync(TEntity entity);
        TEntity InsertOrUpdate(TEntity entity);
        Task<TEntity> InsertOrUpdateAsync(TEntity entity);
        Guid InsertOrUpdateAndGetId(TEntity entity);
        Task<Guid> InsertOrUpdateAndGetIdAsync(TEntity entity);

        //Update Entity
        TEntity Update(TEntity entity);
        Task<TEntity> UpdateAsync(TEntity entity);

        //Delete Entity
        void Delete(TEntity entity);
        Task DeleteAsync(TEntity entity);
        void Delete(Guid id);
        Task DeleteAsync(Guid id);
        void Delete(Expression<Func<TEntity, bool>> predicate);
        Task DeleteAsync(Expression<Func<TEntity, bool>> predicate);

        //Count Entities
        int Count();
        Task<int> CountAsync();
        int Count(Expression<Func<TEntity, bool>> predicate);
        Task<int> CountAsync(Expression<Func<TEntity, bool>> predicate);

        bool Any(Expression<Func<TEntity, bool>> predicate);
    }
}
