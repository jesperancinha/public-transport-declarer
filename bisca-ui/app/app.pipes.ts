//… imports

// We must extend Pipe and JSONPipeFactory to create a singleton pipe first
export class JSONPipeFactory extends Pipe {
  // In this method we must return which objects can be used with this pipe. In this case, we accept any object.
  supports(obj):boolean {
    return true;
  }

  // Given any JSON object, we pretty print it using JSON.stringify
  transform(value):string {
    return JSON.stringify(value, null, 2);
  }

  // This method creates a new pipe. Since we're going to use a singleton pipe, we'll always return this in this case.
  create():Pipe {
    return this;
  }
}

// We need to create a list of all the pipes that will be set when bootstrapping the Angular 2 app. In this case, we'll only have one other than the default ones
import { defaultPipes } from 'angular2/change_detection';
export var pipes = Object.assign({}, defaultPipes, {
  'json': [
    new JSONPipeFactory()
  ]
});