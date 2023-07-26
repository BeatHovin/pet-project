import { Injectable } from '@angular/core'
import {
  HttpClient,
  HttpParams,
} from '@angular/common/http'
import { Product } from '../model/Product/product';
import { environment } from 'src/environments/environment';
import {Observable} from "rxjs";
import {CustomHttpRespone} from "../model/custom-http-response";
import {ConsoleLogger} from "@angular/compiler-cli/ngcc";
import {User} from "../model/user";
import {ProductType} from "../model/Product/productType";
import {NgForm} from "@angular/forms";

@Injectable({providedIn: 'root'})
export class ProductService {
  private host = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getProducts() {
    return this.http.get(`${this.host}/product/productList`)
  }

  public getProductsByProductType(productType: string) {
    return this.http.get(`${this.host}/product/productListByProductType/${productType}`);
  }

  // public addProductToCart(product: Product) {
  //   return this.http.get(`${this.host}/user/addProductToCart/${product}`);
  // }

  public getProductBySpecifications(formData: FormData): Observable<Product> {
    let d = this.http.post<Product>(`${this.host}/product/productBySpecifications`, formData)
    return d;
  }

  public saveOrUpdateProduct(formData: FormData): Observable<Product> {
    let d = this.http.post<Product>(`${this.host}/product/saveOrUpdateProduct`, formData)
    return d;
  }
  public getProductTypesById(productTypeId: number) {
    return this.http.get(`${this.host}/product/productListByProductTypeId/${productTypeId}`);
  }
  getProductTypes() {
    return this.http.get(`${this.host}/product/productTypeList`)
  }


  public createProductFormData(product: NgForm, productType: ProductType, currentUser: User): FormData {
    const formData = new FormData();
    formData.append('productType', productType?.productType);
    formData.append('userEmail', currentUser?.email);
    // @ts-ignore
    formData.append('length', product.length);
    // @ts-ignore
    formData.append('profileSize', product.profileSize);
    // @ts-ignore
    formData.append('sideThickness', product.sideThickness);
    // @ts-ignore
    formData.append('steelGrade', product.steelGrade);
    // @ts-ignore
    formData.append('quantity', product.quantity);
    // @ts-ignore
    formData.append('price', product.price);

    return formData;
  }

  public createProductToCartFormData(productQuantity: NgForm, product:Product): Product {
    let _product = new Product();
   _product = product;
   // @ts-ignore
    _product.quantity = productQuantity.productToCartQuantity;

    return _product;
  }
//   createUser(user: User) {
//     return this.http.post(this.url, user)
//   }
//   updateUser(id: number, user: User) {
//     const urlParams = new HttpParams().set(
//       'id',
//       id.toString()
//     )
//     return this.http.put(this.url, user, {
//       params: urlParams,
//     })
//   }
//   deleteUser(id: number) {
//     return this.http.delete(this.url + '/' + id)
//   }
}
