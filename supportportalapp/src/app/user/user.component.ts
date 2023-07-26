import {Component, OnInit, OnDestroy} from '@angular/core';
import {BehaviorSubject, Subscription} from 'rxjs';
import {User} from '../model/user';
import {UserService} from '../service/user.service';
import {NotificationService} from '../service/notification.service';
import {NotificationType} from '../enum/notification-type.enum';
import {HttpErrorResponse, HttpEvent, HttpEventType, HttpResponse} from '@angular/common/http';
import {NgForm} from '@angular/forms';
import {CustomHttpRespone} from '../model/custom-http-response';
import {AuthenticationService} from '../service/authentication.service';
import {Router} from '@angular/router';
import {Role} from '../enum/role.enum';
import {Product} from '../model/Product/product';
import {ProductType} from '../model/Product/productType';

import {ProductService} from '../service/product.service';
import {OrderService} from '../service/order.service';
import {NewPassword} from "../model/newPassword";
import {Order} from "../model/Order/order";
import {OrderDTO} from "../model/dto/orderDTO";
import {any} from "codelyzer/util/function";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit, OnDestroy {
  public users: User[];
  public products: Product[];
  public productsByProductType: Product[];
  public cart: Product[] = [];
  public orders: Order[] = [];
  public inAcceptingOrders: Order[] = [];
  public inProcessingOrders: Order[] = [];
  public productToCard = new Product();
  public productTypes: ProductType[];
  public selectedProductType: ProductType;
  public user: User;
  public newPassword = new NewPassword;
  public refreshing: boolean;
  public selectedUser: User;
  private subscriptions: Subscription[] = [];
  public editUser = new User();
  public customerToView = new User();
  private currentEmail: string;

  public productsInCart: Order[];

  public uniqueSteelGrades: string[];
  public uniqueSideThicknesses: number[];

  constructor(private router: Router, private authenticationService: AuthenticationService,
              private userService: UserService, private notificationService: NotificationService, private productService: ProductService, private orderService: OrderService) {
  }

  ngOnInit(): void {
    this.loadProductTypes();
    this.user = this.authenticationService.getUserFromLocalCache();
    this.onOpenCart();
    this.loadOrders();
  }

  private onOpenCart() {
    if (localStorage.getItem('cart') != null)
      this.cart = JSON.parse(localStorage.getItem('cart'));
    this.orderService.getOrdersByStatus("in_cart").subscribe((res: Order[]) => {
      this.productsInCart = res;
    });
  }

  loadOrders() {
    if (!this.isManager) {
      this.orderService.getOrdersByEmail(this.user.email).subscribe((data: Order[]) => {
        this.orders = data;
      })
    } else {
     this.orderService.getOrdersByStatus("in_processing").subscribe(data => this.inProcessingOrders = data);
     this.orderService.getOrdersByStatus("in_accepting").subscribe(data => this.inAcceptingOrders = data);
    }

  }

  private loadProductTypes() {
    this.productService.getProductTypes().subscribe((data: ProductType[]) => {
      this.productTypes = data;
    })
  }

  public getUsers(): void {
    this.refreshing = true;
    this.subscriptions.push(
      this.userService.getUsers().subscribe(
        (response: User[]) => {
          this.userService.addUsersToLocalCache(response);
          this.users = response;
          this.refreshing = false;
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
          this.refreshing = false;
        }
      )
    );
  }


  addProductToCard(productQuantity: number) {
    this.productToCard.quantity = productQuantity;
    this.cart.push(this.productToCard);
    this.clickButton("closeAddToCartModalButton");
    localStorage.setItem('cart', JSON.stringify(this.cart));
    this.sendNotification(NotificationType.SUCCESS, `Товар добавлен в карзину`);
  }

  public onAddToCart(product: Product): void {
    this.productToCard = Object.assign({}, product);
    this.clickButton('addToCart');
  }

  saveOrUpdateProduct(product: NgForm) {
    const formData = this.productService.createProductFormData(product.value, this.selectedProductType, this.user);

    this.productService.saveOrUpdateProduct(formData).subscribe();

  }

  onClearCart() {
    this.cart = [];
    localStorage.removeItem('cart');
    this.sendNotification(NotificationType.SUCCESS, `Корзина очищина`);
  }

  public changePassword(newPassword: NewPassword): void {
    const formData = this.userService.createPasswordFormDate(this.user.email, newPassword);
    this.subscriptions.push(
      this.userService.updatePassword(formData).subscribe(
        (response: NewPassword) => {
          this.clickButton('closeUpdateUserPassword');
          this.getUsers();
          this.sendNotification(NotificationType.SUCCESS, `Пароль обновлен`);
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
        }
      )
    );
  }

  public loadProductsByProductType(productType: string) {
    this.productService.getProductsByProductType(productType).subscribe((data: Product[]) => {
      this.productsByProductType = data;
    })
  }


  public onUpdateCurrentUser(user: User): void {
    this.refreshing = true;
    user.role = this.user.role;
    user.notLocked = this.user.notLocked;
    user.active = this.user.active;
    this.currentEmail = this.authenticationService.getUserFromLocalCache().email;
    const formData = this.userService.createUserFormDate(this.currentEmail, user);
    this.subscriptions.push(
      this.userService.updateUser(formData).subscribe(
        (response: User) => {
          this.authenticationService.addUserToLocalCache(response);
          this.getUsers();
          this.sendNotification(NotificationType.SUCCESS, `Данные обновлены`);
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
          this.refreshing = false;
        }
      )
    );
  }

  public onLogOut(): void {
    this.authenticationService.logOut();
    this.router.navigate(['/login']);
  }

  public showProductsByProductType(productType: ProductType): void {
    this.selectedProductType = productType;
    this.loadProductsByProductType(productType.productType);
    this.clickButton('showProductsByProductType');
  }

  public onChangePassword(): void {
    this.clickButton('openUpdateUserPassword');

  }

  public getOrderPrice(order:Order): number{
    var orderPrice = 0;
    for (let product of order.orderDetails){
      orderPrice+= product.product.price * product.quantity;
    }
    return orderPrice;
  }

  public onViewUser(user: User) {
    this.customerToView = user;
    this.clickButton('openCustomerView');
  }

  public onAcceptOrder(order: Order) {
    this.orderService.acceptOrder(order.id).subscribe((data: HttpResponse<any>) => {
      this.sendNotification(NotificationType.SUCCESS, "Заказ передан в сборку");
    })
    this.inAcceptingOrders = this.inAcceptingOrders.filter(item => item !== order);
  }

  public onDenyOrder(order: Order) {
    this.orderService.denyOrder(order.id).subscribe((data: HttpResponse<any>) => {
      this.sendNotification(NotificationType.SUCCESS, data.statusText);
    })
  }

  public onProcessOrder(order: Order) {
    this.orderService.processOrder(order.id).subscribe((data: HttpResponse<any>) => {
      this.sendNotification(NotificationType.SUCCESS, "Заказ завершен");
    })
    this.inProcessingOrders = this.inProcessingOrders.filter(item => item !== order);

  }

  public get isAdmin(): boolean {
    return this.getUserRole() === Role.ADMIN;
  }

  public get isUser(): boolean {
    return this.getUserRole() === Role.USER;
  }

  public get isManager(): boolean {
    return this.getUserRole() === Role.MANAGER;
  }

  private getUserRole(): string {
    return this.authenticationService.getUserFromLocalCache().role;
  }

  private sendNotification(notificationType: NotificationType, message: string): void {
    if (message) {
      this.notificationService.notify(notificationType, message);
    } else {
      this.notificationService.notify(notificationType, 'ОШИБКА)))');
    }
  }

  private clickButton(buttonId: string): void {
    document.getElementById(buttonId).click();
  }

  onConfirmOrder() {
    this.orderService.postOrder(new OrderDTO(this.cart, this.user.email)).subscribe(
      (response: Product[]) => {
      },
      (errorResponse: HttpErrorResponse) => {

      }
    );
    this.onClearCart();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }


  editProduct(productType: ProductType) {
    this.selectedProductType = productType;
    this.loadProductsByProductType(productType.productType);
    this.clickButton('editProduct');
  }
}
