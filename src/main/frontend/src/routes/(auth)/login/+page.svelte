<script context="module">
    import {title} from "../../+layout.svelte";
</script>

<script lang="ts">
    import Card from "$lib/components/Card.svelte";
    import Input from "$lib/components/Input.svelte";
    import Checkbox from "$lib/components/Checkbox.svelte";
    import Button from "$lib/components/Button.svelte";

    $title = "Login";

    let form = {
        email: '',
        password: ''
    }

    const onSubmit = async () => {
        try {
            const res = await fetch('http://127.0.0.1/api/auth/authenticate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(form)
            });
            const data = await res.json();
            console.log(data);
        } catch(error) {
            // enter your logic for when there is an error (ex. error toast)
            console.log(error)
        }
    };


</script>

<Card
        title="Log In">
    <span slot="content">

        <form on:submit|preventDefault={onSubmit}>
            <div>
                <Input
                        type="email"
                        label="Email"
                        placeholder="email"
                        required
                        autofocus
                        autocomplete="username"
                        bind:value={form.email}
                />
            </div>
            <div>
                <Input
                        type="password"
                        label="Password"
                        placeholder="password"
                        required
                        autocomplete="current-password"
                        bind:value={form.password}
                />
            </div>

            <Checkbox label="Remember me"/>

            <div class="flex items-center justify-end mt-4">

                    <a href="/resetPassword" class="link link-hover">
                        Reset password
                    </a>

                    <Button class="ml-4" type="submit" disabled={false}>
                        LOG IN
                    </Button>
            </div>
        </form>

    </span>

</Card>