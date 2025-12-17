import CryptoJS from 'crypto-js';

export function validateEmail(rule, value, callback) {
  const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
  if (!emailRegex.test(value)) {
    callback(new Error("请输入有效的邮箱地址"));
  } else {
    callback();
  }
}

export function validatePhone(rule, value, callback) {
  const phoneRegex = /^1[3-9]\d{9}$/;
  if (!phoneRegex.test(value)) {
    callback(new Error("请输入有效的手机号"));
  } else {
    callback();
  }
}

export function validateConfirmPassword(rule, value, callback) {
  const form = this.registerForm;
  if (value !== form.password) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
}

export function encrypt(data) {
  const secretKey = 'linsslinss123456'; 
  const encryptedData = CryptoJS.AES.encrypt(JSON.stringify(data), secretKey).toString();
  return { encryptedData };
}