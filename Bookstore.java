
package contents.servlet;

import contents.bean.Book;
import contents.bean.Page;
import contents.service.BookService;
import contents.service.impl.BookServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author LiuDuo
 * @DateTime 2020/5/20 19:46
 * @Version 1.0
 * @DevelopmentTools IntelliJ IDEA 2019.3.4
 * @Desc:
 */
public class BookServlet extends BaseServlet {
    private BookService bookService = new BookServiceImpl();

    protected void getAllBooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 查询所有的数据
        List<Book> books = bookService.getAllBooks();
        //数据共享,把图书信息到Request域中
        request.setAttribute("books", books);
        //路径跳转(转发)
        request.getRequestDispatcher("/pages/manager/book_manager.jsp").forward(request, response);

    }


    protected void deleteBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 获取请求的参数 id
        String id = request.getParameter("id");
        //2 调用bookService.deleteBook(id) 删除图书
        bookService.deleteBook(id);
        // 3 重定向回图书列表管理页面
        response.sendRedirect(request
                .getContextPath() + "/BookServlet?method=getAllBooksByPage");
    }

    protected void getBookById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1 获取请求的参数图书信息
        String id = request.getParameter("id");
        //2 调用 bookService.getBookById(id):Book图书信息;
        Book book = bookService.getBookById(id);
        // 3 保存图书信息到Request域中
        request.setAttribute("book", book);
        // 4 请求转发到book_edit.jsp页面
        request.getRequestDispatcher("/pages/manager/book_update.jsp")
                .forward(request, response);
    }

    protected void saveOrUpdateBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//修改book信息，跳转（不应该直接跳转到book_manager.jsp显示数据，应该重新调用查询方法。）
        //1. 获取指定参数,img_path设置默认值
        String title = request.getParameter("book_name");
        String author = request.getParameter("book_author");
        String price = request.getParameter("book_price");
        String sales = request.getParameter("book_sales");
        String stock = request.getParameter("book_stock");
        //与添加区别
        String id = request.getParameter("id");
        if (id == null || "".equals(id)) {
            //添加
            bookService.saveBook(new Book(null, title, author, Double.parseDouble(price), Integer.parseInt(sales), Integer.parseInt(stock), null));
        } else {
            //修改
            bookService.updateBook(new Book(Integer.parseInt(id), title, author, Double.parseDouble(price), Integer.parseInt(sales), Integer.parseInt(stock), null));
        }
        //跳转
        response.sendRedirect(request.getContextPath() + "/BookServlet?method=getAllBooksByPage");
    }

    /*分页查询Book信息*/
    protected void getAllBooksByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从jsp中获取参数
        String pageNo = request.getParameter("pageNo");

        //调用service中的方法
        Page<Book> page = bookService.getBooksByPage(pageNo);
        //将数据存放到域中，共享
        request.setAttribute("page", page);

        //跳转
        request.getRequestDispatcher("/pages/manager/book_manager.jsp").forward(request, response);
    }

    @Data

}

